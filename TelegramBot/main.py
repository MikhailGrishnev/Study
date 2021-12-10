import telebot
from telebot import types
token = '5039454406:AAGN9a9l2qrFPkm2O_cpCuN1tFEoyQAJ_I8'
bot = telebot.TeleBot(token)


@bot.message_handler(commands=['start'])
def start(message):
    keyboard = types.ReplyKeyboardMarkup(True)
    keyboard.row('ПН ВЕРХ', 'ПН НИЗ')
    keyboard.row('ВТ ВЕРХ', 'ВТ НИЗ')
    keyboard.row('СР ВЕРХ', 'СР НИЗ')
    keyboard.row('ЧТ ВЕРХ', 'ЧТ НИЗ')
    keyboard.row('ПТ ВЕРХ', 'ПТ НИЗ')
    keyboard.row('СБ ВЕРХ', 'СБ НИЗ')
    keyboard.row('Зачеты', 'Экзамены')
    keyboard.row('/help')
    bot.send_message(message.chat.id,
                     'Привет! Я бот, созданный для показа расписания занятий, зачетов и экзаменов! Что именно тебя интересует?',
                     reply_markup=keyboard)


@bot.message_handler(commands=['help'])
def start_message(message):
    bot.send_message(message.chat.id, 'Я умею показывать расписание на выбранный учебный день недели, а также экзаменов и зачетов. В будущем возможно появление списка со всеми данными на дом заданиями.')


@bot.message_handler(content_types=['text'])
def main(message):
    id = message.chat.id
    msg = message.text
    if msg == 'ПН ВЕРХ' or msg == 'ПН НИЗ':
        bot.send_message(id, '11:25 - КГ практика - сдать работы;\n '
                             '15:25 - ВычТех практика;\n '
                             '17:10 - Философия практика;\n '
                             '19:00 - ВвИТ практика ')

    elif msg == 'ВТ ВЕРХ':
        bot.send_message(id, 'Занятий нет')


    elif msg == 'ВТ НИЗ':
        bot.send_message(id, '11:25 - Физ-ра; \n'
                             '13:10 - ВвИТ лаб;\n '
                             '15:25 - ВвИТ лаб; \n'
                             '17:10 - ВвИТ лекция ')

    elif msg == 'СР ВЕРХ':
        bot.send_message(id, '11:25 - Физ-ра; \n'
                             '13:10 - Философия лекция;\n '
                             '15:25 - КГ лекция ')

    elif msg == 'СР НИЗ':
        bot.send_message(id, '9:30 - Английский;\n '
                             '11:25 - Английский ')

    elif msg == 'ЧТ ВЕРХ':
        bot.send_message(id, '11:25 - Физ-ра;\n '
                             '13:10 - ВычТеч лекция;\n '
                             '15:25 - ВысшМат лекция;\n '
                             '17:10 - АГиЛА лекция ')

    elif msg == 'ЧТ НИЗ':
        bot.send_message(id, '11:25 - ИнфЭк лекция;\n '
                             '13:10 - ВычТеч лекция; \n'
                             '15:25 - ИнфЭк лаб;\n '
                             '17:10 - Физ-ра')

    elif msg == 'ПТ ВЕРХ' or msg == 'ПТ НИЗ':
        bot.send_message(id, 'Выходной')

    elif msg == 'СБ ВЕРХ' or msg == 'СБ НИЗ':
        bot.send_message(id, '9:30 - ВысшМат;\n '
                             '11:25 - АГиЛА ')

    elif msg == 'Зачеты':
        bot.send_message(id, '20.12 - КГ; \n'
                             '14.12 - ИнфЭк;\n '
                             '22.12 - Физ-ра;\n '
                             '15.12 - Английский;\n '
                             '20-25.12 - ВычТех')

    elif msg == 'Экзамены':
        bot.send_message(id, 'Неизвестно  - ВысшМат;\n '
                             'Неизвестно  - АГиЛА;\n '
                             'Неизвестно  - ВвИТ;\n '
                             'Неизвестно - Философия ')

# bot.infinity_polling(none_stop=True)


bot.infinity_polling()