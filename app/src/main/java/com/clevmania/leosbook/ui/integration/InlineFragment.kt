package com.clevmania.leosbook.ui.integration

import android.annotation.SuppressLint
import android.net.http.SslError
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.clevmania.leosbook.R
import com.clevmania.leosbook.constants.AppKeys
import com.clevmania.leosbook.constants.Constants
import com.clevmania.leosbook.data.FirebaseUtils
import com.clevmania.leosbook.ui.base.TopLevelFragment
import com.clevmania.leosbook.utils.InjectorUtils
import com.clevmania.leosbook.utils.UiUtils
import kotlinx.android.synthetic.main.inline_fragment.*
import org.json.JSONObject
import kotlin.math.roundToInt


class InlineFragment : TopLevelFragment() {

    companion object {
        fun newInstance() = InlineFragment()
    }

    private lateinit var viewModel: InlineViewModel
    private lateinit var viewModelFactory: InlineViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelFactory = InjectorUtils.provideInlineViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory).get(InlineViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.inline_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = arguments?.getString("userName")!!
        val mail = arguments?.getString("userMail")!!
        val mobile = arguments?.getString("userMobile")!!
        val amount = arguments?.getDouble("totalCost")!!.roundToInt()

        val byteData = initPaymentViaFlutterWaveInline(mail, mobile, name, amount).toByteArray()
        loadWebView(byteData)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(viewModel) {
            progress.observe(viewLifecycleOwner, Observer { uiEvent ->
                uiEvent.getContentIfNotHandled()?.let {
                    toggleBlockingProgress(it)
                }
            })

            error.observe(viewLifecycleOwner, Observer { uiEvent ->
                uiEvent.getContentIfNotHandled()?.let {
                    showErrorDialog(it)
                }
            })

            verificationResponse.observe(viewLifecycleOwner, Observer { uiEvent ->
                uiEvent.getContentIfNotHandled()?.let {
                    if (it.status == Constants.API_TRANSACTION_SUCCESS) {
                        showTransactionSuccessDialog("Transaction Successful")
                        FirebaseUtils.getUID()?.let { uid -> clearCart(uid) }
                    }
                }
            })
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebView(data: ByteArray) {
        wvFlutterInline.settings.javaScriptEnabled = true
        wvFlutterInline.settings.domStorageEnabled = true
        wvFlutterInline.settings.setSupportMultipleWindows(false)
        wvFlutterInline.settings.javaScriptCanOpenWindowsAutomatically = false
//        wvFlutterInline.settings.setSupportZoom(true)
//        wvFlutterInline.settings.builtInZoomControls = true
        wvFlutterInline.webViewClient = declareWebViewClient()
        wvFlutterInline.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
//        wvFlutterInline.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary))
        wvFlutterInline.addJavascriptInterface(
            WebPaymentCallbackInterface(this), "Android"
        )

        val encodedHtml = Base64.encodeToString(data, Base64.NO_PADDING)
        wvFlutterInline.loadData(encodedHtml, "text/html", "base64")
    }

    private fun initPaymentViaFlutterWaveInline(email : String, phone: String, name: String, amount : Int) : String{
        return """
            <!DOCTYPE html>
            <html>
                <head>
                    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                    <script src="https://checkout.flutterwave.com/v3.js"></script>
                    <script>
                      function makePayment() {
                        FlutterwaveCheckout({
                          public_key: "${AppKeys.PUBLIC_KEY}",
                          tx_ref: "${UiUtils.randomReferenceGenerator()}",
                          amount: $amount,
                          currency: "NGN",
                          payment_options: " ${PaymentOptions.card},${PaymentOptions.banktransfer}, ${PaymentOptions.ussd}",
                          //redirect_url: // specified redirect URL
                            //"https://callbacks.piedpiper.com/flutterwave.aspx?ismobile=34",
                          meta: {
                            consumer_id: 23,
                            consumer_mac: "92a3-912ba-1192a",
                          },
                          customer: {
                            email: "$email",
                            phone_number: "$phone",
                            name: "$name",
                          },
                          onclose: function() {
                            console.log("Flutter wave closePaymentPage");
                            Android.closePaymentPage();
                          },
                          callback: function (data) {
                            console.log(data);
                            Android.verifyTransaction(JSON.stringify(data))
                          },
                          customizations: {
                            title: "Leos Book",
                            description: "Payment for items in cart",
                            logo: "https://assets.piedpiper.com/logo.png",
                          },
                        });
                      }
                    </script>
                </head>

                <body onload="makePayment()"> </body>
            </html>
        """
    }

    private fun closePage() {
        findNavController().popBackStack(R.id.bookStoreFragment, false)
    }

    private fun verifyPayment(id: String) {
        viewModel.verifyTransactionStatus(id)
    }

    private fun declareWebViewClient(): WebViewClient {
        return object : WebViewClient() {
            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                super.onReceivedSslError(view, handler, error)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
            }
        }
    }

    class WebPaymentCallbackInterface(private val ctx: InlineFragment) {

        @JavascriptInterface
        fun verifyTransaction(data: String) {
            val responseObject = JSONObject(data)
            if (responseObject.has("transaction_id")) {
                val id = responseObject.getString("transaction_id")
                ctx.verifyPayment(id)
            }
        }

        @JavascriptInterface
        fun closePaymentPage() {
            ctx.closePage()
        }
    }

}

enum class PaymentOptions {
    account, card, ussd, transfer, banktransfer, qr, barter, credit
}