import java.sql.*;
import java.io.*;
import java.util.*;
import java.net.*;






public class DBConnect{
    public static void main(String[] args) throws Exception{
        


        // String url = "https://dd.weather.gc.ca/air_quality/aqhi/ont/observation/realtime/csv/2020070200_AQHI_ON_SiteObs.csv";

        

        // Timer time = new Timer(false);

        // time.schedule(new TimerTask(){
        //         public void run(){
        //             // downloadUsingStream(url, "./ontario.csv");
        //             System.out.println(new java.util.Date());
        //         }
        // },new java.util.Date(),1000*60*60);

        Connection connection = null;
        PreparedStatement statement;
        int batchSize = 20;

        String url = "jdbc:postgresql://localhost:5432/transnomis";
        String user = "postgres";
        String password = "651125";

        try {
 
            try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // List list = readCSV();

        connection = DriverManager.getConnection(url,user,password);

        connection.setAutoCommit(false);
        String sql = "INSERT INTO observation (date, hour, faffd,falif,falji) VALUES (?, ?, ?,?,?)";
        statement = connection.prepareStatement(sql);
        BufferedReader lineReader = new BufferedReader(new FileReader("./ontario.csv"));

        String lineText = null;
 
            int count = 0;
 
            lineReader.readLine(); 
 
            while ((lineText = lineReader.readLine()) != null) {
                count++;
                String[] data = lineText.split(",");
                String date = data[0];
                String hour = data[1];
                String faffd = data[2];
                String falif = data[3];
                String falji = data[4];
                
                statement.setString(1, date);
                statement.setString(2, hour);
                statement.setString(3, faffd);
                statement.setString(4, falif);
                statement.setString(5, falji);
 
                statement.addBatch();
 
                if (count % batchSize == 0) {
                    statement.executeBatch();
                }
            }
            lineReader.close();
            statement.executeBatch();

            connection.commit();
            connection.close();
 
        } catch (IOException ex) {
            System.err.println(ex);
        } catch (SQLException ex) {
            ex.printStackTrace();
 
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
 
    }




        
        



    

    

    public static ArrayList readCSV() {
        ArrayList<String> list = new ArrayList<String>();

        try {
            
            BufferedReader reader = new BufferedReader(new FileReader("./ontario.csv"));
            reader.readLine();
            String line = null;
            while ((line = reader.readLine()) != null) {
               
               
                list.add(line);
                
                // int value = Integer.parseInt(last);

                // list.add(last);
                // System.out.println(last);
            }
            System.out.println(list);
            return list;
            
        } 
        
        catch (Exception e) {

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



