# Running this project in dev env
On linux with docker installed, or WSL with integrated docker desktop, just run the following command in the cloned project directory:
`docker compose -f docker-compose.dev.yml up --build -d`. Linux/WSL is required since spring hot-reloads depend on inotifywait.
## On windows as newbie

* In powershell: `wsl --install ubuntu`
* After completing instalation clone repo with: `https://github.com/Muszchrom/unhealthy-cherry.git`
* Cd into project directory: `cd unhealthy-cherry`
* Open project in VS Code `code .` (Other IDEs might work too.) 
* In docker desktop, go to: Settings > Resources > WSL integration and check `Enable integration with my default WSL distro` and `Enable integration with additional distros`: `Ubuntu`
* Go back to Ubuntu (or open vs code console) and try running `docker` command. You should see bunch of text. If you've got an error, try restarting PC.
* Finally build and run containers: `docker compose -f docker-compose.dev.yml up --build -d`

If you're having trouble with getting back to ubuntu CLI try:
* Typing ubuntu in windows search bar
* (Win 11) Clicking down-facing arrow next to "Open a new tab" and selecting ubuntu
* Running `wsl --distribution <Distribution Name> --user <User Name>` in PS

## Frontend
create Frontend/.env.local file and provide `GITHUB_ID` and `GITHUB_SECRET` variables

## Backend
* Invalid requests typically result in responses with `text/plain;charset=UTF-8` Content-Type.
* Always use @Column("name") annotation since for example R2DBC reads isAdmin as is_admin instead of isadmin
* Read `Database` section from readme.md
* Admin should be created manually in pgadmin.
* Only admin can create new users 
* REST (by Roy Fielding deffinition) is currently lacking HATEOAS, example response should look like: 

```
HTTP/1.1 200 OK
{
    "account": {
        "account_number": 12345,
        "balance": {
            "currency": "usd",
            "value": 100.00
        },
        "links": {
            "deposits": "/accounts/12345/deposits",
            "withdrawals": "/accounts/12345/withdrawals",
            "transfers": "/accounts/12345/transfers",
            "close-requests": "/accounts/12345/close-requests"
        }
    }
}
```

### /categories
* 😎 @Get
* 🛡️ @Post
* 🛡️ @Put +/{id}
* 🛡️ @Delete +/{id}

### /places
* 😎 @Get
* 😎 @Get + ?categoryid=
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

## Gateway
This is basically an auth wrapper for backend

### /login
* 😎 @Post

### /logout
* 😎 @Get

### /user (todo)
* 🛡️ @Post - PRIO (register new user)
* 🛡️ @Get
* 🛡️ @Patch
* 🛡️ @Delete

### bugs
* anyone can get user by id

## pgAdmin4

### Connecting to PostgreSQL server
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
* Tables are created in `InitDatabase` directory
* If you want to edit .sql files, you will need to rebuild the database container after making the changes.
