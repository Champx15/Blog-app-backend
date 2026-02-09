package com.champ.Service;

import com.google.api.client.auth.oauth2.Credential;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {


    public void sendHtmlEmail(String to, String otp) throws Exception {

        String htmlContent = """
                <html>
                  <head>
                    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
                    <style>
                      body {
                        font-family: Arial, sans-serif;
                        background-color: #ffffff;
                        color: #333333;
                        margin: 0;
                        padding: 0;
                        text-align: center;
                      }
                      .container {
                        max-width: 600px;
                        margin: 40px auto;
                        background-color: #ffffff;
                        border: 1px solid #e0e0e0;
                        border-radius: 8px;
                        padding: 30px 30px;
                        box-shadow: 0 2px 8px rgba(0,0,0,0.05);
                      }
                      h2 {
                        font-size: 26px;
                        font-weight: bold;
                        margin-bottom: 15px;
                      }
                      p {
                        font-size: 15px;
                        color: #555555;
                        margin: 10px 0;
                      }
                      .otp-box {
                        display: inline-block;
                        background-color: #f9f9f9;
                        border: 1px solid #ddd;
                        border-radius: 6px;
                        padding: 15px 25px;
                        font-size: 24px;
                        font-weight: bold;
                        color: #d93025;
                        letter-spacing: 4px;
                        margin: 25px 0;
                      }
                      a {
                        color: #10a37f;
                        text-decoration: none;
                      }
                      .footer {
                        font-size: 12px;
                        color: gray;
                        margin-top: 25px;
                      }
                      @media (prefers-color-scheme: dark) {
                        body {
                          background-color: #121212;
                          color: #e0e0e0;
                        }
                        .container {
                          background-color: #1e1e1e;
                          border-color: #333;
                        }
                        .otp-box {
                          background-color: #2c2c2c;
                          border-color: #444;
                          color: #ff6b6b;
                        }
                        a {
                          color: #58d3a0;
                        }
                        .footer {
                          color: #aaaaaa;
                        }
                      }
                    </style>
                  </head>
                  <body>
                    <div class="container">
                      <h3>Verify your email address</h3>
                      <p>To continue setting up your Editorial account, please enter and verify the given otp.</p>
                      <div class="otp-box">%s</div>
                      <p >This code will expire after 5 minutes. If you did not make this request, please disregard this email.</p>
                      <p class="footer">&copy; 2025 Editorial. All rights reserved.</p>
                    </div>
                  </body>
                </html>
                """.formatted(otp);

        String plainText = "Your OTP code for Editorial is: " + otp;

        // 2️⃣ Authorize Gmail (headless mode if on Render)
        Credential credential = OAuthService.authorize();
        String accessToken = credential.getAccessToken();

        // 3️⃣ Create the HTML email using GmailService
        MimeMessage email = GmailService.createEmailHtml(to, "me", "Verify your Editorial account", htmlContent, plainText);

        // 4️⃣ Send email via Gmail API
        GmailService.sendEmail(accessToken, to, "me", "Verify your Editorial account", htmlContent);

    }
}
