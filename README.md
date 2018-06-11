# VkWallToTelegramMessages

## How to use?

First of all, you will need to get `id`
of the chat which must receive messages. How to do that?

1. Open and start dialog with `@ShowJsonBot`
2. Detect id
    * If you want to receive in private messages
        1. Write to bot
        2. Find chat id by the way
           ```
           {
               "message": {
                   "from": {
                       "chat": {
                           "id": IT IS YOUR ID
                       }
                   }
                }
            }
           ```
