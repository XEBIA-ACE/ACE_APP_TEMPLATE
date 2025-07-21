require('dotenv').config();
const app = require('./app');
const connectDB = require('./config/db');
const redisClient = require('./config/redis');

const PORT = process.env.PORT || {{cookiecutter.default_port}};

connectDB();
redisClient.connect();

app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
}); 