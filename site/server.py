from flask import Flask, render_template

app = Flask(__name__)

# Rota para a página web
@app.route('/')
def index():
    return render_template('index.html')

# Executa o servidor
if __name__ == '__main__':
    app.run(debug=True)
