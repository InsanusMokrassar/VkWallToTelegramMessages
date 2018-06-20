# VkWallToTelegramMessages

## Warning

Before using tis bot, please read [Ограничения и рекомендации](https://vk.com/dev/api_requests?f=3.%20%D0%9E%D0%B3%D1%80%D0%B0%D0%BD%D0%B8%D1%87%D0%B5%D0%BD%D0%B8%D1%8F%20%D0%B8%20%D1%80%D0%B5%D0%BA%D0%BE%D0%BC%D0%B5%D0%BD%D0%B4%D0%B0%D1%86%D0%B8%D0%B8) or [Api requests](https://vk.com/dev/api_requests) (section `Limits and recommendations`)

## How to use?

For using this bot you will need to fill four main parameters:

* `accessToken` - Token for work with `VK API`. You can read about getting this token [here](https://vk.com/dev/access_token)
* `botApiToken` - Token for work with `Telegram Bot API`. Open [`How do I create a bot`](https://core.telegram.org/bots#3-how-do-i-create-a-bot)
small manual (it is simple). Remember that all what you need - to get `bot API token` and put it in the settings
* `VK Group`
    * `wallOwnerId` - If your target is group with domain as `club123456789` you must get the number
    after word `club` and put into `wallOwnerId` in config with minus (`-123456789`, for example). For users
    you will get `id123456789` number after `id` word and put into config WITHOUT `-` (`123456789`)
    * `wallDomain` - Domain of wall which you want to receive if previous variant
    is not for you in the telegram. For example, in address `https://vk.com/example`
`wallDomain` is `example`
* `chatId` - ID number of group/channel/private chat which must receive messages from VK wall

### `chatId` or `How te get it?`

1. Open and start dialog with `@ShowJsonBot` in `Telegram`
2. Start dialog with this bot
3. How to get id
    * If you want to receive in your private messages
        1. Write to bot
        2. Find chat id by the way
            ```json
            {
                "message": {
                    "from": {
                        "chat": {
                            "id": 123123123
                        }
                    }
                }
            }
            ```
    * If you want to receive messages in channel/group/private
        1. Forward message from channel to bot
        2. asdasd
            ```json
            {
                "message": {
                    "forward_from_chat": {
                        "id": -1001286316435
                    }
                }
            }
            ```

## Conclusion

As a result you must have next config:

```json
{
  "accessToken":"Some your access token which was got from vk application management page",
  "botApiToken": "123456789:ABCDEFGHIJKLMNOPQRTSUVWXYZXYZXYZXYZ",
  "wallDomain": "Some domain of VK wall or",
  "wallOwnerId": 1,
  "updateDelay": 60000,
  "chatId": -100500100500100500
}
```
