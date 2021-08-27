# Discord BOT
This is one of my biggest projects so far, as you already know it's a bot for Discord platform with a lot of features and commands that allow you to fully control and customize your discord server. I have uploaded only essential files because most of them are only available for my own use. This project is finished but I'm still updating it and adding new features.

## API Integration
There is a lot of APIs available, but of course one of the most important is JDA that allows to run and manage this bot. Other APIs:
* YouTube
* Twitch
* Giphy
* NASA
* FiveM
* Lavaplayer
* IP Validation powered by [IP Intelligence](http://getipintel.net/)
## AI
One of the cool things is that the bot has access to AI characters so it can chat with everyone. I'm currently using ready conversational model since I had some trouble with creating my own because something was wrong with a code. In the future, I'm planning to implement and train my own model. To chat with AI all you need to do is place `&` symbol before your message, like: `& how you doing?`
## Worth seeing
Since there is a lot of things I have listed the most worth seeing below:
* [Twitch API](https://github.com/wherearethehoneyberries/discord-bot/blob/main/src/main/java/API/Twitch.java) - allows adding channels later stored in the database to display them when LIVE. It removes offline channels and leaves the LIVE ones only. This feature is quite old so don't mind my code here, but It works.
* [Content Verification](https://github.com/wherearethehoneyberries/discord-bot/blob/main/src/main/java/Admin/ContentVerification.java) - useful for big community servers (still work in progress), every image send by regular user is being removed and send to specified channel for admins to verify it. After verification image is send back to the original channel and available for everyone.
* [Economy](https://github.com/wherearethehoneyberries/discord-bot/tree/main/src/main/java/Economy) - works just like a bank, it supports account creation, money management and transfers.
* [Games](https://github.com/wherearethehoneyberries/discord-bot/tree/main/src/main/java/Games) - TicTacToe, Vending Machine, etc.
* [Inventory](https://github.com/wherearethehoneyberries/discord-bot/tree/main/src/main/java/Inventory)
* [Music](https://github.com/wherearethehoneyberries/discord-bot/tree/main/src/main/java/Music)
