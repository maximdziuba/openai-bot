# OpenAI Telegram Bot #

Bot uses OpenAI-API to generate images. Bot gets the prompt from user and generates the image.

## Running ##
To run the app you need to create your telegram-bot with BotFather and get the OpenAI-API key to access api.
In the project tokens and api-keys are specified in the environment, you can also specify these values in ```application.properties```

``` 
git clone https://github.com/maximdziuba/openai-bot.git
cd <project-folder>
bot_token=<Your telegram-bot token> bot_username=<Your telegram-bot's usename> openai_key=<your OpenAi-API key> ./gradlew bootRun
```
