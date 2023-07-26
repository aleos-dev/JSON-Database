# JSON Database Project

This project implements a client-server architecture to interact with a JSON-based database. Using simple command-line arguments, you can perform various CRUD operations on the database.

## Features:
- CRUD Operations: Set, Get, Delete, and Exit.
- Multi-threaded server handling for concurrent requests.
- Supports nested JSON structures.
- Clear and concise command-line interactions.
  

## Usage:

### Starting the Server:
Run the main server class:

```bash
java server.Main
```

You should see:

```bash
Server started!
```

### Using the Client:
Run the client with appropriate command-line arguments:

#### Set:
```bash
java client.menu.Client -t set -k person -v "{\"name\":\"Elon Musk\",\"car\":{\"model\":\"Tesla Roadster\"},\"rocket\":{\"name\":\"Falcon 9\",\"launches\":\"88\"}}"
```

#### Get:
```bash
java client.menu.Client -t get -k person
```

#### Delete:
```bash
java client.menu.Client -t delete -k ["person","car","year"]
```

#### Exit:
```bash
java client.menu.Client -t exit
```

**Note**: You can also pass requests using the `-in` flag followed by a filename containing the JSON request.

## Examples:

1. **Setting a Value**:
   ```bash
   java client.menu.Client -t set -k person -v "{\"name\":\"Elon Musk\"}"
   ```

   This will set the "person" key in the database to the value `{"name":"Elon Musk"}`.

2. **Getting a Value**:
   ```bash
   java client.menu.Client -t get -k person
   ```

   This will retrieve the value associated with the "person" key.

3. **Deleting a Nested Key**:
   ```bash
   java client.menu.Client -t delete -k ["person","car","year"]
   ```

   This will delete the "year" key nested inside "car" which is nested inside "person".

4. **Exiting the Server**:
   ```bash
   java client.menu.Client -t exit
   ```

   This will shut down the server.

