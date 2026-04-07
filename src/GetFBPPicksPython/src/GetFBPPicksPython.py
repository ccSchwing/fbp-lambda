import json
import os
import boto3
import logging
import ast
from decimal import Decimal
from botocore.exceptions import ClientError
from aws_lambda_powertools.utilities.data_classes import APIGatewayProxyEvent


'''
This function will return the user information for the given email address in the event.
'''

logger = logging.getLogger()
logger.setLevel(logging.INFO)

FBP_PICKS_TABLE_NAME = os.environ.get('FBPPicksTableName', 'FBP-Picks')


def decimal_default(value):
    if isinstance(value, Decimal):
        # Preserve whole numbers as ints; keep non-whole values as floats.
        return int(value) if value % 1 == 0 else float(value)
    raise TypeError(f"Object of type {type(value).__name__} is not JSON serializable")

def lambda_handler(event, context):
    try:
        apigw_event = APIGatewayProxyEvent(event)
        # logger.info("Received API Gateway event keys: %s", sorted(event.keys()))
        body = apigw_event.json_body
        if body is None:
            raw_body = event.get('body')
            if isinstance(raw_body, str) and raw_body:
                try:
                    body = json.loads(raw_body)
                except json.JSONDecodeError:
                    body = ast.literal_eval(raw_body)
            elif isinstance(raw_body, dict):
                body = raw_body
            else:
                body = {}
        logger.info(f"Parsed JSON body: {body}")
        email = body.get('email')
        logger.info(f"Extracted email from API Gateway event: {email}")

    except Exception as e:
        logger.error(f"Unexpected error: {e}")
        return {
            'statusCode': 400,
            'body': json.dumps({
                'error': 'Invalid request body',
                'message': 'Request body must be valid JSON with an email field'
            })
        }

    if not email:   
        return {
            'statusCode': 400,
            'body': json.dumps({
                'error': 'Email address is required',
                'message': 'Please provide email address in the event'
            })
        }
    item = get_fbp_picks(email)
    
    if item:
        # Convert common top-level numeric fields for readability.
        if 'week' in item:
            item['week'] = int(item['week'])
        if 'tieBreaker' in item:
            item['tieBreaker'] = int(item['tieBreaker'])
        return {
            'statusCode': 200,
            'body': json.dumps({
                'email': item.get('email'),
                'displayName': item.get('displayName'),
                'picks': item.get('picks'),
                'tieBreaker': item.get('tieBreaker'),
                'week': item.get('week')
                }, default=decimal_default)
            }
    else:
        logger.info(f"User not found: {email}")
        return {
            'statusCode': 404,
            'body': json.dumps({
                'error': f'User with email {email} not found',
                'email': email
                })
            } 
        

def get_fbp_picks(emailAddress):
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(FBP_PICKS_TABLE_NAME)
    try:
        response = table.get_item(
            Key={'email': emailAddress}
        )
        item = response['Item'] if 'Item' in response else None
        return item
    except ClientError as e:
        logger.error(f"DynamoDB Error: {e}")
        return None
    except Exception as e:
        logger.error(f"Unexpected error: {e}")
        return None
