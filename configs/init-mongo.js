if (!db.getCollectionNames().includes('user')) {
    db.createCollection('user');
}

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
