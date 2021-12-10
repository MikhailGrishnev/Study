import requests
from flask import Flask, render_template, request, redirect
import psycopg2

app = Flask(__name__)
conn = psycopg2.connect(database="service_db",
                        user="mich",
                        password="MichPwd1",
                        host="localhost",
                        port="5432")

cursor = conn.cursor()
#     cursor.execute(f"SELECT * FROM service.users WHERE login='{username}' AND password='{password}'")


@app.route('/login/', methods=['POST', 'GET'])
def login():
    if request.method == 'POST':
        if request.form.get("login"):
            username = request.form.get('username')
            password = request.form.get('password')
            # cursor.execute("SELECT * FROM service.users WHERE login=%s AND password=%s", (str(username), str(password)))
            cursor.execute(f"SELECT * FROM service.users WHERE login='{username}' AND password='{password}'")
            records = list(cursor.fetchall())
            print("=" * 20)
            print(f"Login by:\nUser: {username}\nPassword: {password}\nRecords: {records}")
            print("=" * 20)

            if username and password and records:
                return render_template('account.html', full_name=records[0][1], log=username, pas=password)
        elif request.form.get("registration"):
            return redirect("/registration/")

    return render_template('login.html')


@app.route('/registration/', methods=['POST', 'GET'])
def registration():
    if request.method == 'POST':
        name = request.form.get('name')
        login = request.form.get('login')
        password = request.form.get('password')
        if str(name) and str(login) and str(password):
            print("=" * 20)
            print(f"Registration for:\nUser: {name}\nLogin: {login}\nPassword: {password}")
            print("=" * 20)
            # cursor.execute("SELECT * FROM service.users WHERE login=%s", (str(login),))
            cursor.execute(f"SELECT * FROM service.users WHERE login='{login}'")
            records = cursor.fetchall()
            if records:
                return redirect("/registration/")
            else:
                # cursor.execute('insert into service.users (full_name, login, password) VALUES (%s, %s, %s);', (str(name), str(login), str(password)))
                cursor.execute(
                    f"insert into service.users (full_name, login, password) "
                    f"VALUES ('{name}', '{login}', '{password}');")
                conn.commit()
                return redirect('/login/')

    return render_template('registration.html')
