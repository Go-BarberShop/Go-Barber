<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Promoção Especial</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
            font-family: Arial, sans-serif;
            color: #333333;
        }

        .container {
            width: 100%;
            max-width: 600px;
            margin: 0 auto;
            background-color: #ffffff;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .header {
            background-color: #333333;
            color: #ffffff;
            text-align: center;
            padding: 20px;
        }

        .header h1 {
            margin: 0;
            font-size: 24px;
            font-weight: bold;
        }

        .content {
            padding: 20px;
            align-content: center;
            text-align: center;
        }

        .promotion-name {
            font-size: 22px;
            font-weight: bold;
            color: #ff6600;
            margin-bottom: 10px;
        }

        .details {
            font-size: 16px;
            color: #666666;
            line-height: 1.5;
            margin-bottom: 20px;
        }

        .combo-price {
            font-size: 20px;
            font-weight: bold;
            color: #ff6600;
            margin-bottom: 30px;
        }

        .cta-button {
            display: inline-block;
            padding: 15px 30px;
            background-color: #ff6600;
            color: #ffffff;
            text-decoration: none;
            font-size: 18px;
            border-radius: 4px;
            transition: background-color 0.3s ease;
        }

        .cta-button:hover {
            background-color: #e65c00;
        }

        .footer {
            background-color: #333333;
            color: #ffffff;
            text-align: center;
            padding: 10px;
            font-size: 12px;
        }

        .footer a {
            color: #ff6600;
            text-decoration: none;
        }
    </style>
</head>

<body>
<div class="container">
    <div class="header">
        <h1>Promoção Especial para Você!</h1>
    </div>
    <div class="content">
        <div class="promotion-name"> ${nomePromocao} </div>
        <div class="combo-price">
            Valor do Combo: R$ ${precoPromocao}
            <br>
            Até o dia: ${endDate}
        </div>
        <a href="#" class="cta-button">Use o Cupom: ${cupom}</a>
    </div>
    <div class="footer">
        <p>Se você não deseja mais receber esses e-mails, clique <a href="#">aqui</a> para cancelar a inscrição.</p>
        <p>© 2024 GoBarber. Todos os direitos reservados.</p>
    </div>
</div>
</body>

</html>