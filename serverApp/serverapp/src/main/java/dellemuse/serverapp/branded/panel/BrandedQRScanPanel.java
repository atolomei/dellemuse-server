package dellemuse.serverapp.branded.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;

import dellemuse.serverapp.page.model.ObjectModelPanel;

public class BrandedQRScanPanel extends ObjectModelPanel<Void> {

    private static final long serialVersionUID = 1L;

    public BrandedQRScanPanel(String id) {
        super(id, null);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        AjaxLink<Void> link = new AjaxLink<Void>("link") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                target.appendJavaScript("openQRModal();");
            }
        };

        link.add(new org.apache.wicket.AttributeModifier("class", "btn border bg-dark"));
        add(link);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        // Librería QR
        response.render(JavaScriptHeaderItem.forUrl("https://unpkg.com/html5-qrcode"));

        response.render(JavaScriptHeaderItem.forScript(
            """
            let qrScannerInstance = null;

            function openQRModal() {
                let modal = document.getElementById("qr-modal");

                if (!modal) {
                    modal = document.createElement("div");
                    modal.id = "qr-modal";
                    modal.innerHTML = `
                        <div id="qr-overlay"></div>
                        <div id="qr-content">
                            <button id="qr-close">✕</button>
                            <div id="qr-reader"></div>
                            <div id="qr-text">QR Scan</div>
                        </div>
                    `;
                    document.body.appendChild(modal);

                    document.getElementById("qr-close").onclick = closeQRModal;
                }

                modal.style.display = "flex";

                startQRScanner();
            }

            function closeQRModal() {
                const modal = document.getElementById("qr-modal");
                modal.style.display = "none";

                if (qrScannerInstance) {
                    qrScannerInstance.stop().catch(() => {});
                    qrScannerInstance = null;
                }
            }

            function startQRScanner() {
                qrScannerInstance = new Html5Qrcode("qr-reader");

                qrScannerInstance.start(
                    { facingMode: "environment" },
                    {
                        fps: 10,
                        qrbox: 250
                    },
                    (decodedText) => {
                        console.log("QR:", decodedText);

                        closeQRModal();

                        // Redirección
                        window.location.href = decodedText;
                    },
                    (errorMessage) => {}
                ).catch(err => {
                    alert("No se pudo acceder a la cámara");
                    console.error(err);
                });
            }
            """,
            "qr-modal-js"
        ));

        // CSS del modal
        response.render(JavaScriptHeaderItem.forScript(
            """
            const style = document.createElement('style');
            style.innerHTML = `
            #qr-modal {
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                display: none;
                align-items: center;
                justify-content: center;
                z-index: 9999;
            }

            #qr-overlay {
                position: absolute;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0.8);
            }

            #qr-content {
                position: relative;
                background: #000;
                border:1px solid #999999;
                padding: 30px 2px 2px 2px;
                border-radius: 12px;
                text-align: center;
                z-index: 2;
                width: 340px;
            }

            #qr-reader {
                width: 300px;
                margin: auto;
            }

            #qr-text {
                color:#eeeeee;
                margin-top: 5px;
                font-size: 12px;
            }

            #qr-close {
                position: absolute;
                top: 3px;
                right: 3px;
                background: transparent;
                border: none;
                color: #eeeeee;
                font-size: 12px;
                cursor: pointer;
            }
            `;
            document.head.appendChild(style);
            """,
            "qr-modal-css"
        ));
    }
}