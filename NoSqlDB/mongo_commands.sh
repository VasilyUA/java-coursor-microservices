#!/bin/bash

# Connect to MongoDB using Docker
docker exec -i mongodb mongosh -u root -p 55555 --authenticationDatabase admin NoSqlDB <<EOF

if (!db.getCollectionNames().includes('user')) {
  print('\nCreate the collection:');
  db.createCollection('user');
} else {
  print('\nClear collection:');
  db.user.drop();
  db.createCollection('user');
}

print('\nCreate 5 users:');
db.user.insertMany([
  {
    firstName: 'John',
    lastName: 'Doe',
    email: 'john.doe@example.com',
    age: 25,
    isMarried: false
  },
  {
    firstName: 'Jane',
    lastName: 'Doe',
    email: 'jane.doe@example.com',
    age: 29,
    isMarried: true
  },
  {
    firstName: 'Alice',
    lastName: 'Smith',
    email: 'alice.smith@example.com',
    age: 31,
    isMarried: true
  },
  {
    firstName: 'Bob',
    lastName: 'Johnson',
    email: 'bob.johnson@example.com',
    age: 13,
    isMarried: false
  },
  {
    firstName: 'Charlie',
    lastName: 'Brown',
    email: 'charlie.brown@example.com',
    age: 12,
    isMarried: true
  }
]);

print('\nFind a user by firstName, lastName, and email:');
db.user.findOne({ firstName: 'John', lastName: 'Doe', email: 'john.doe@example.com' });

print('\nFind users older than 18:');
db.user.find({ age: { '\$gt': 18 } });

print('\nFind married users:');
db.user.find({ isMarried: true });
EOF
