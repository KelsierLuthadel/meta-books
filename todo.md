# TODO

## Database

create views

## Pagination
Return previous/next link  
min - max values  


## Books
add:ratings to BookDetails


## Error handling
better exception response for migration errors

## Testing
SQLi testing  
XSS testing  
unit tests for BookDetails  
test 404 for api tests /{id}

## Searching
Custom fields   
Desired response body
Date search, including ranges  

## Admin
roles, users: Set constraints and add swagger API details (from books)  



# REST

## Authors
POST /api/1/authors => [{id: 1, name:name}]

GET /api/1/authors/{id} => {id:1, name:name}
GET /api/1/authors/{id}/books => [{id:1, title:book}]

## Books
POST /api/1/books => [{id:1, title:book}]

GET /api/1/{id} => {id:1, title:book}
GET /api/1/{id}/metadata => {id:1, title:book, cover: cover, comments: comment}


