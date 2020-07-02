import java.sql.*;
import java.io.*;
import java.util.*;
import java.net.*;



public class DBConnect{
    public static void main(String[] args) throws Exception{
        // Connection con;
        // Statement st;
        // ResultSet rs;

        // String url = "jdbc:postgresql://localhost:5432/transnomis";
        // String user = "postgres";
        // String password = "651125";

        // try {
        //     Class.forName("org.postgresql.Driver");
        // } catch (ClassNotFoundException e) {
        //     e.printStackTrace();
        // }

        // con = DriverManager.getConnection(url,user,password);
        // st = con.createStatement();

        // rs = st.executeQuery("select date,hour from observation");
        // while(rs.next()){
        //     System.out.print(rs.getString("date")+",");
        //     System.out.println(rs.getString("hour"));
        // }
        // readCSV();

        // rs.close();
        // st.close();
        // con.close();


        String url = "https://dd.weather.gc.ca/air_quality/aqhi/ont/observation/realtime/csv/2020070200_AQHI_ON_SiteObs.csv";

        try {
           

            downloadUsingStream(url, "./ontario.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    public static List readCSV() {
        try {
            ArrayList<String> list = new ArrayList<String>();
            BufferedReader reader = new BufferedReader(new FileReader("./ON_sample.csv"));
            // reader.readLine();
            String line = null;
            while ((line = reader.readLine()) != null) {
                String item[] = line.split(",");
                String last = item[item.length - 1];
                // int value = Integer.parseInt(last);

                list.add(last);
                // System.out.println(last);
            }
            System.out.println("data of the line：" + list);
            return list;
        } catch (Exception e) {
        }
        return null;
        
    }

    public static void downloadUsingStream(String urlStr, String file) throws IOException{
        URL url = new URL(urlStr);
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileOutputStream fis = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int count=0;
        while((count = bis.read(buffer,0,1024)) != -1)
        {
            fis.write(buffer, 0, count);
        }
        fis.close();
        bis.close();
    }



}