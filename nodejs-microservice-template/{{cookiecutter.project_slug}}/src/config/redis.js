const { createClient } = require('redis');
const REDIS_HOST = process.env.REDIS_HOST || '{{cookiecutter.redis_host}}';
const REDIS_PORT = process.env.REDIS_PORT || '{{cookiecutter.redis_port}}';

const client = createClient({
  url: `redis://${REDIS_HOST}:${REDIS_PORT}`
});

client.on('error', (err) => console.error('Redis Client Error', err));

module.exports = client; 