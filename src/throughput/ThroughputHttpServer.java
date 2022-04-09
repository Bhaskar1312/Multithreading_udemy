package throughput;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThroughputHttpServer {
    public static final String INPUT_FILE = "resources/war_and_peace.txt";
    public static final int NUMBER_OF_THREADS = 8;
    public static void main(String[] args) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(INPUT_FILE)));
        startServer(text);
    }

    public static void startServer(String text) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/search", new WordCountHandler(text));
        Executor executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        server.setExecutor(executor);
        server.start();

    }
    public static class WordCountHandler implements HttpHandler {
        private final String text;

        public WordCountHandler(String text) {
            this.text = text;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            System.out.println(query);
            String[] keyValue = query.split("=");
            String action = keyValue[0];
            String word = keyValue[1];
            if(!action.equals("word")) {
                exchange.sendResponseHeaders(400, 0);
                return;
            }
            long count = countWord(word);
            System.out.println("word "+ word + " appeared " +count+ " times");

            byte[] response = Long.toString(count).getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, response.length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response);
            outputStream.close();
        }
//        private int[] prefixFunction(String word) {
//            int[] pi = new int[word.length()+1];
//            pi[0] = 0;
//            for(int i=1;i<word.length();i++) {
//                int j = pi[i-1];
//                while(j>0 && word.charAt(i) != word.charAt(j)) {
//                    j = pi[j-1];
//                }
//                if(word.charAt(i) == word.charAt(j)) j++;
//                pi[i] = j;
//            }
//            System.out.println(Arrays.toString(pi));
//            return pi;
//        }
        private int countWord(String word) {
            int count =0;
            int index =0;
            while(index >=0) {
                index = text.indexOf(word, index);
                if(index >= 0) {
                    count++;
                    index++;
                }
            }
            return count;
        }
    }
}
//jmeter incomplete