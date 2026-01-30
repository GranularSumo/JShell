# JavaShell

A custom shell implementation exploring how command-line interfaces work under the hood.

_Post-MSc learning project investigating shell internals, parsing, and I/O management in Java._

## Learning Journey

Building this taught me that shells are more complex than simple command forwarders:

- **Parsing**: Tokenizing user input while handling quotes and special characters
- **Process Management**: Creating and managing child processes
- **I/O Orchestration**: Setting up redirections and file descriptors
- **Command Resolution**: PATH searching and built-in vs external command logic

## What It Can Do

### Interactive Shell

```bash
[/home/user] $ echo "Hello World" > output.txt
[/home/user] $ pwd
/home/user
[/home/user] $ cd projects
[/home/user/projects] $ type echo
echo is a shell builtin
```

### Built-in Commands

- `echo` - Output text and variables
- `pwd` - Show current directory
- `cd` - Change directories (supports `~` expansion)
- `exit` - Terminate the shell
- `type` - Check if command is built-in or external

### I/O Redirection

```bash
# Output redirection
$ echo "data" > file.txt
$ echo "more data" >> file.txt

# Error redirection
$ invalid_command 2> errors.log
$ command > output.txt 2> errors.log

# Combined output and errors
$ command &> all_output.txt
```

### External Commands

```bash
ls -la
python script.py
git status
```

_Automatically searches PATH and executes external programs_

## How It Works

```
Input → Lexer → Parser → Evaluator → Output
```

**Lexer**: State machine for tokenization (handles quotes, redirects)  
**Parser**: Converts tokens to command structures  
**Evaluator**: Executes with proper I/O setup

The parsing currently creates simple command objects, but I learned that implementing a proper Abstract Syntax Tree (AST) would make complex features like pipelines much more manageable - that's my next learning goal.

## Running It

### Prerequisites

- Gradle 9.2.0+

### Build and Run

```bash
# Clone and build
git clone <your-repo-url>
cd JavaShell
./gradlew build

# Start interactive shell
./run.sh
```

### Example Session

```bash
[/current/dir] $ echo "Testing redirection" > test.txt
[/current/dir] $ type echo
echo is a shell builtin
[/current/dir] $ cat test.txt
Testing redirection
[/current/dir] $ exit
```

## Future Learning

- AST implementation for complex parsing (pipelines, operators)
- Inter-process communication for pipe operations
- Variable systems and environment management

## Project Structure

```
org.example/
├── lexer/     # Tokenization with state machine
├── parser/    # Command structure creation from tokens
├── commands/  # Built-in command implementations
└── evaluator/ # Command execution and I/O handling
```

