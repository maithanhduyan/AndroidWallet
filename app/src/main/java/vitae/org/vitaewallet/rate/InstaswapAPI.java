package vitae.org.vitaewallet.rate;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * Created by Hemant Singh on 30-06-2019.
 */

public class InstaswapAPI {
    private static final String URL = "https://api.instaswap.io";

    public String getVitaeDepositAddress() throws RequestVitaeRateException {
        try {
            String bigDecimal = null;
            String url = this.URL + "/tickers?giveCoin=VITAE&getCoin=BTC";
            BasicHttpParams basicHttpParams = new BasicHttpParams();
            HttpConnectionParams.setSoTimeout(basicHttpParams, (int) TimeUnit.MINUTES.toMillis(1));
            HttpClient client = new DefaultHttpClient(basicHttpParams);
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Accept", "text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
            httpGet.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = client.execute(httpGet);
            InputStream inputStream = null;
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            String result = null;
            if (inputStream != null) result = convertInputStreamToString(inputStream);

            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                JSONArray jsonArray = new JSONArray(result);
                bigDecimal = new String(jsonArray.getJSONObject(0).getString("price_usd"));
            }
            return bigDecimal;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw new RequestVitaeRateException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RequestVitaeRateException(e);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RequestVitaeRateException(e);
        }
    }

    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream,"ISO-8859-1"));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }


}


