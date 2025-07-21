FROM node:{{cookiecutter.node_version}}-alpine

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

EXPOSE {{cookiecutter.default_port}}

CMD ["npm", "start"] 