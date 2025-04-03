# Server Protocol

## In

First line: Full Name  
Second Line: Type of request  
Third Line:  Data (JSON string)

## Out

First Line: Response status (SUCCESS/ERROR)  
Second Line Onwards: Data

## Request Types

- GET_QUIZ_DATA => gets the data for the quiz
    - Accepts: {"chapterNum": int}
    - Returns: String with CSV file contents
