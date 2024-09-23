# TODO
* Both app and gateway databases should be created and initialized in InitDatabase/*.sql files
* ### MOVE THAT FUCKING DB CREATION FROM BACKEND TO INIT_USER_DATABSE>SQL
* add testing

# Running the app
docker compose -f docker-compose.dev.yml up --build -d

# Backend
* Invalid requests typically result in responses with `text/plain;charset=UTF-8` Content-Type.
* Always use @Column("name") annotation since for example R2DBC reads isAdmin as is_admin instead of isadmin
* Read `Database` section from readme.md
* Admin should be created manually in pgadmin.
* Only admin can create new users

### /categories
* 😎 @Get
* 🛡️ @Post
* 🛡️ @Put +/{id}
* 🛡️ @Delete +/{id}

### /places
* 😎 @Get
* 🛡️ @Post
* 🛡️ @Put +/{id}
* 🛡️ @Delete +/{id}

### /photo/{fileName}
* 😎 @Get

### /photos
* 😎 @Get
* 😎 @Get +/{category}
* 😎 @Get +/{category}/{place}
* 🛡️ @Post
* 🛡️ @Patch +/{id}
* 🛡️ @Delete +/{id}

# Gateway
This is basically an auth wrapper for backend

### /login
* 😎 @Post

### /logout
* 😎 @Get

### /register (todo)
* 🛡️ @Get
* 🛡️ @Post - PRIO
* 🛡️ @Patch
* 🛡️ @Delete

# pgAdmin4

## Connecting to PostgreSQL server
* On host go to `localhost:5420`
* Right click Servers, located on the left side of browser window, then select Register, server
* Fill in Name field with whatever name you like
* Go to Connection section
  * Host name/address: `host.docker.internal`
  * Port: `5432`
  * Maintenance database: `student`
  * Username: `student`
  * Password: `student`

## Databse
* user database is created in InitDatabase/init-user-database.sql
* admin is created in InitDatabase/init-user-database.sql
* if you want to edit init-user-database.sql, you would need to delete this database container and rebuild the app. Doing so requires backend app restart, since Photos db is created in THAT JAVA APP FFS!!!!

