import discord
import os

PREFIX_LENGTH = 7
FORMAT_PREFIX = "* B Beta III IV I AXLE\n"
TEST_PATH = os.path.abspath("..")
TEST_PATH = os.path.join(TEST_PATH, "enigma/testing")
CWD = os.path.abspath(".")

client = discord.Client()
class BotClient(discord.Client):
    @client.event
    async def on_message(self, message):
        prefix = message.content[:PREFIX_LENGTH]
        msg_content = (message.content[PREFIX_LENGTH:]).trim()
        if (prefix == '$enigma'):
            os.chdir(TEST_PATH)
            format_file = open('content.in', 'w')
            f.write(FORMAT_PREFIX)
            f.write(msg.content + "\n")
            f.close()
            os.chdir("..")
            //Run enigma files
            //parse content

        else:
            return

client = BotClient()
client.run('token value')

















