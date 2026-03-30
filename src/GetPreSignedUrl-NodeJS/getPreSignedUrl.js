const { S3Client, GetObjectCommand } = require("@aws-sdk/client-s3");
const { getSignedUrl } = require("@aws-sdk/s3-request-presigner");

const s3Client = new S3Client({ region: process.env.AWS_REGION });
const bucketName = process.env.S3_BUCKET_NAME;

exports.handler = async (event) => {
    console.log("Bucket Name from Environment Variable:", bucketName);

    if(event.httpMethod !== 'OPTIONS') {
        console.log('Received', event.httpMethod, 'request');
        return {
            statusCode: 200,
            headers: {
                'Access-Control-Allow-Origin': 'https://my-fbp.com',
                'Access-Control-Allow-Headers': 'Content-Type',
                'Content-Type': 'application/json'
            },
            body: ""
        };
    }

    console.log('Bucket Name:', bucketName);

    console.log('Query Parameters:', event.queryStringParameters);

    console.log('Received request for file key:', event.queryStringParameters?.key);

    console.log('Event:', JSON.stringify(event, null, 2));
    
    try {
        // Get the file key from query parameters
        const fileKey = event.queryStringParameters?.key;
        if (!fileKey) {
            return {
                statusCode: 400,
                headers: {
                    'Access-Control-Allow-Origin': 'https://my-fbp.com',
                    'Access-Control-Allow-Headers': 'Content-Type',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ 
                    error: 'Missing required parameter: key' 
                })
            };
        }

        // Create the S3 command
        const command = new GetObjectCommand({
            Bucket: bucketName,
            Key: fileKey
        });

        // Start the presigned URL promise but do not await yet
        const presignedUrlPromise = getSignedUrl(s3Client, command, { expiresIn: 3600 });

        // Do logging and any other work here before awaiting
        console.log('Created GetObjectCommand for bucket:', bucketName, 'and key:', fileKey);
        console.log('Event:', JSON.stringify(event, null, 2));

        // Now await the presigned URL
        let presignedUrl;
        try {
            presignedUrl = await presignedUrlPromise;
            console.log('Generated presigned URL:', presignedUrl);
        } catch (error) {
            console.error('Error generating presigned URL:', error);
            throw error; // Rethrow to be caught by outer catch
        }
        if(!presignedUrl) {
            console.error('Failed to generate presigned URL, got undefined');
            throw new Error('Failed to generate presigned URL');
        }
        if(null === presignedUrl) {
            console.error('Failed to generate presigned URL, got null');
            throw new Error('Failed to generate presigned URL');
        }
        if(presignedUrl.length === 0) {
            console.error('Failed to generate presigned URL, got empty string');
            throw new Error('Failed to generate presigned URL');
        }
        return {
            statusCode: 200,
            headers: {
                'Access-Control-Allow-Origin': 'https://my-fbp.com',
                'Access-Control-Allow-Headers': 'Content-Type',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                url: presignedUrl,
                expires: new Date(Date.now() + 3600000).toISOString()
            })
        };
    } catch (error) {
        console.error('Error:', error);
        console.error('Event:', JSON.stringify(event, null, 2));
        return {
            statusCode: 500,
            headers: {
                'Access-Control-Allow-Origin': 'https://my-fbp.com',
                'Access-Control-Allow-Headers': 'Content-Type',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ 
                error: 'Failed to generate presigned URL',
                message: error.message 
            })
        };
    }
};
