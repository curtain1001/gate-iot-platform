package net.pingfang.common.utils.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.springframework.web.multipart.MultipartFile;

public class HttpUtils2 {

	public static HttpResponse doGet(String host, String path, Map<String, String> headers, Map<String, String> querys)
			throws Exception {
		HttpClient httpClient = wrapClient(host);

		HttpGet request = new HttpGet(buildUrl(host, path, querys));
		for (Entry e : headers.entrySet()) {
			request.addHeader((String) e.getKey(), (String) e.getValue());
		}

		return httpClient.execute(request);
	}

	public static HttpResponse doPost(String host, String path, Map<String, String> headers, Map<String, String> querys,
			Map<String, String> bodys) throws Exception {
		HttpClient httpClient = wrapClient(host);

		HttpPost request = new HttpPost(buildUrl(host, path, querys));
		Entry e;
		for (Iterator localIterator = headers.entrySet().iterator(); localIterator.hasNext();) {
			e = (Entry) localIterator.next();
			request.addHeader((String) e.getKey(), (String) e.getValue());
		}

		if (bodys != null) {
			Object nameValuePairList = new ArrayList();

			for (String key : bodys.keySet()) {
				((List) nameValuePairList).add(new BasicNameValuePair(key, (String) bodys.get(key)));
			}
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity((List) nameValuePairList, "utf-8");
			formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
			request.setEntity(formEntity);
		}

		return (HttpResponse) httpClient.execute(request);
	}

	private static HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
		@Override
		public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
			return false;
		}
	};

	public static HttpResponse doPost(String host, String path, Map<String, String> headers, Map<String, String> querys,
			String body) throws Exception {
		HttpClient httpClient = wrapClient(host);
		HttpPost request = new HttpPost(buildUrl(host, path, querys));
		RequestConfig config = RequestConfig.custom() //
				.setConnectTimeout(5000) //
				.setConnectionRequestTimeout(5000)//
				.build(); //
		request.setConfig(config);
		for (Entry e : headers.entrySet()) {
			request.addHeader((String) e.getKey(), (String) e.getValue());
		}
		if (StringUtils.isNotBlank(body)) {
			request.setEntity(new StringEntity(body, "utf-8"));
		}
		return httpClient.execute(request);
	}

	public static HttpResponse doPost(String host, String path, String method, Map<String, String> headers,
			Map<String, String> querys, byte[] body) throws Exception {
		HttpClient httpClient = wrapClient(host);

		HttpPost request = new HttpPost(buildUrl(host, path, querys));
		for (Entry e : headers.entrySet()) {
			request.addHeader((String) e.getKey(), (String) e.getValue());
		}

		if (body != null) {
			request.setEntity(new ByteArrayEntity(body));
		}

		return httpClient.execute(request);
	}

	public static HttpResponse doPut(String host, String path, String method, Map<String, String> headers,
			Map<String, String> querys, String body) throws Exception {
		HttpClient httpClient = wrapClient(host);

		HttpPut request = new HttpPut(buildUrl(host, path, querys));
		for (Entry e : headers.entrySet()) {
			request.addHeader((String) e.getKey(), (String) e.getValue());
		}

		if (StringUtils.isNotBlank(body)) {
			request.setEntity(new StringEntity(body, "utf-8"));
		}

		return httpClient.execute(request);
	}

	public static HttpResponse doPut(String host, String path, String method, Map<String, String> headers,
			Map<String, String> querys, byte[] body) throws Exception {
		HttpClient httpClient = wrapClient(host);

		HttpPut request = new HttpPut(buildUrl(host, path, querys));
		for (Entry e : headers.entrySet()) {
			request.addHeader((String) e.getKey(), (String) e.getValue());
		}

		if (body != null) {
			request.setEntity(new ByteArrayEntity(body));
		}

		return httpClient.execute(request);
	}

	public static HttpResponse doDelete(String host, String path, String method, Map<String, String> headers,
			Map<String, String> querys) throws Exception {
		HttpClient httpClient = wrapClient(host);

		HttpDelete request = new HttpDelete(buildUrl(host, path, querys));
		for (Entry e : headers.entrySet()) {
			request.addHeader((String) e.getKey(), (String) e.getValue());
		}

		return httpClient.execute(request);
	}

	private static String buildUrl(String host, String path, Map<String, String> querys)
			throws UnsupportedEncodingException {
		StringBuilder sbUrl = new StringBuilder();
		sbUrl.append(host);
		if (!StringUtils.isBlank(path)) {
			sbUrl.append(path);
		}
		if (null != querys) {
			StringBuilder sbQuery = new StringBuilder();
			for (Entry query : querys.entrySet()) {
				if (0 < sbQuery.length()) {
					sbQuery.append("&");
				}
				if ((StringUtils.isBlank((CharSequence) query.getKey()))
						&& (!StringUtils.isBlank((CharSequence) query.getValue()))) {
					sbQuery.append((String) query.getValue());
				}
				if (!StringUtils.isBlank((CharSequence) query.getKey())) {
					sbQuery.append((String) query.getKey());
					if (!StringUtils.isBlank((CharSequence) query.getValue())) {
						sbQuery.append("=");
						sbQuery.append(URLEncoder.encode((String) query.getValue(), "utf-8"));
					}
				}
			}
			if (0 < sbQuery.length()) {
				sbUrl.append("?").append(sbQuery);
			}
		}

		return sbUrl.toString();
	}

	private static HttpClient wrapClient(String host) {
		HttpClient httpClient = HttpClients.custom().setRetryHandler(retryHandler).build();
		if (host.startsWith("https://")) {
			return sslClient(httpClient);
		}
		return httpClient;
	}

//	private static void sslClient(HttpClient httpClient) {
//		try {
//			SSLContext ctx = SSLContext.getInstance("TLS");
//			X509TrustManager tm = new X509TrustManager() {
//				public X509Certificate[] getAcceptedIssuers() {
//					return null;
//				}
//
//				public void checkClientTrusted(X509Certificate[] xcs, String str) {
//				}
//
//				public void checkServerTrusted(X509Certificate[] xcs, String str) {
//				}
//			};
//			ctx.init(null, new TrustManager[] { tm }, null);
//			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
//			ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//			ClientConnectionManager ccm = httpClient.getConnectionManager();
//			SchemeRegistry registry = ccm.getSchemeRegistry();
//			registry.register(new Scheme("https", 443, ssf));
//		} catch (KeyManagementException | NoSuchAlgorithmException ex) {
//			throw new RuntimeException(ex);
//		}
//	}

	private static HttpClient sslClient(HttpClient httpClient) {
		try {
			TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
					NoopHostnameVerifier.INSTANCE);

			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();

			BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(
					socketFactoryRegistry);
			return HttpClients.custom().setSSLSocketFactory(sslsf).setConnectionManager(connectionManager).build();
		} catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
			throw new RuntimeException(e);
		}
	}

	public static HttpResponse uploadFile(String host, String path, Map<String, String> headers,
			Map<String, String> querys, Map<String, MultipartFile> fileParams) throws IOException {
		HttpClient httpClient = wrapClient(host);

		HttpPost request = new HttpPost(buildUrl(host, path, querys));
		for (Entry e : headers.entrySet()) {
			request.addHeader((String) e.getKey(), (String) e.getValue());
		}
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setCharset(StandardCharsets.UTF_8);
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);// 加上此行代码解决返回中文乱码问题
		// 文件传输http请求头(multipart/form-data)
		if (fileParams != null && fileParams.size() > 0) {
			for (Entry<String, MultipartFile> e : fileParams.entrySet()) {
				String fileParamName = e.getKey();
				MultipartFile file = e.getValue();
				if (file != null) {
					String fileName = file.getOriginalFilename();
					builder.addBinaryBody(fileParamName, file.getInputStream(), ContentType.MULTIPART_FORM_DATA,
							fileName);// 文件流
				}
			}
		}
		return httpClient.execute(request);// 执行提交

	}
}
