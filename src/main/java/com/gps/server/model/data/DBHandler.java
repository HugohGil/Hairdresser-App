package com.gps.server.model.data;

import com.gps.server.Register;
import com.gps.shared_resources.*;
import com.gps.shared_resources.responses.*;
import com.gps.shared_resources.utils.CellType;
import javafx.util.Pair;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import java.util.regex.Pattern;


public class DBHandler {
    private static final String VALIDATION_CODE = "1234";
    private final Connection connection;

    public DBHandler() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + "db.db");
    }

    public synchronized boolean login(Request request, ObjectOutputStream oos) {
        String msg;
        try {
            Pair<String, String> info = (Pair<String, String>) request.getRequest();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT id,email,password,autenticado,funcionario FROM utilizador"
            );

            while (result.next()) {
                int id = result.getInt("id");
                String email = result.getString("email");
                String password = result.getString("password");
                int autenticado = result.getInt("autenticado");
                int funcionario = result.getInt("funcionario");

                 if (email.equals(info.getKey()) && password.equals(info.getValue())) {
                    if (autenticado == 1) {
                        msg = "O user já esta logado";
                        System.out.println(msg);
                        oos.writeObject(new LoginResponse(null, false, msg));
                        return false;
                    }else if(funcionario == 1){
                        msg = "Utilize aplicação para funcionários para entrar com esta conta";
                        System.out.println(msg);
                        oos.writeObject(new LoginResponse(null, false, msg));
                        return false;
                    }
                    msg = "O cliente foi logado com sucesso";
                    System.out.println(msg);
                    oos.writeObject(new LoginResponse(id, true, msg));
                    statement.executeUpdate("UPDATE utilizador SET autenticado = 1 WHERE id = '" + id + "';");

                    return true;
                }

            }

            msg = "O username ou a password estão incorretas";
            System.out.println(msg);
            oos.writeObject(new LoginResponse(null, false, msg));

            statement.close();
            result.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public synchronized boolean loginWorker(Request request, ObjectOutputStream oos) {
        String msg;
        try {
            Pair<String, String> info = (Pair<String, String>) request.getRequest();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT id,email,password,autenticado,funcionario FROM utilizador"
            );

            while (result.next()) {
                int id = result.getInt("id");
                String email = result.getString("email");
                String password = result.getString("password");
                int autenticado = result.getInt("autenticado");
                int funcionario = result.getInt("funcionario");

                if (email.equals(info.getKey()) && password.equals(info.getValue())) {
                    if (autenticado == 1) {
                        msg = "O user já esta logado";
                        System.out.println(msg);
                        oos.writeObject(new LoginResponse(null, false, msg));
                        return false;
                    }else if(funcionario == 0){
                        msg = "Utilize aplicação para clientes para entrar com esta conta";
                        System.out.println(msg);
                        oos.writeObject(new LoginResponse(null, false, msg));
                        return false;
                    }
                    msg = "O cliente foi logado com sucesso";
                    System.out.println(msg);
                    oos.writeObject(new LoginResponse(id, true, msg));
                    statement.executeUpdate("UPDATE utilizador SET autenticado = 1 WHERE id = '" + id + "';");

                    return true;
                }

            }

            msg = "O username ou a password estão incorretas";
            System.out.println(msg);
            oos.writeObject(new LoginResponse(null, false, msg));

            statement.close();
            result.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized boolean register(Request request, ObjectOutputStream oos) {
        String msg;
        Register register = (Register) request.getRequest();
        boolean isUnique = true;
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(
                    "SELECT email,nome FROM utilizador"
            );

            while (result.next()) {
                String email = result.getString("email");
                String nome = result.getString("nome");
                if (email.equals(register.getEmail()) || nome.equals(register.getName())) {
                    isUnique = false;
                }
            }

            if (isUnique) {
                if (!(register.getEmail().contains("@") && register.getEmail().contains(".com"))) {
                    msg = "O email tem de estar no formato nome@domain.com";
                    System.out.println(msg);
                    oos.writeObject(new RegisterResponse(false, msg, "email"));
                    return false;
                } else if (register.getName().length() < 5 || register.getName().length() > 50) {
                    msg = "O nome tem de ter [5,50] caracteres";
                    System.out.println(msg);
                    oos.writeObject(new RegisterResponse(false, msg, "nome"));
                    return false;
                } else if (!(!register.getPassword().equals(register.getPassword().toLowerCase()) && register.getPassword().length() >= 10)) {
                    msg = "A password tem de ter 1 maiúscula e no mínimo 10 caracteres";
                    System.out.println(msg);
                    oos.writeObject(new RegisterResponse(false, msg, "password"));
                    return false;
                } else if (register.getDate().after(new Date())) {
                    msg = "Insira uma data inferior ao dia de hoje";
                    System.out.println(msg);
                    oos.writeObject(new RegisterResponse(false, msg, "date"));
                    return false;
                }else if (register.getValidationCode() != null){
                    if(!register.getValidationCode().equals(VALIDATION_CODE)){
                        msg = "Código de Validação errado";
                        System.out.println(msg);
                        oos.writeObject(new RegisterResponse(false, msg, "código de validação"));
                        return false;
                    }
                }

                if(register.getValidationCode() == null) {
                    System.out.println("NO VALIDATION CODE");
                    statement.executeUpdate("INSERT INTO utilizador(email,nome,password,autenticado)"
                            + "VALUES("
                            + "'" + register.getEmail() + "',"
                            + "'" + register.getName() + "',"
                            + "'" + register.getPassword() + "',"
                            + "'" + 0 + "');");
                }
                else {
                    System.out.println("VALIDATION CODE");
                    statement.executeUpdate("INSERT INTO utilizador(email,nome,password,autenticado,funcionario)"
                            + "VALUES("
                            + "'" + register.getEmail() + "',"
                            + "'" + register.getName() + "',"
                            + "'" + register.getPassword() + "',"
                            + "'" + 0 + "',"
                            + "'" + 1 + "');");
                }

                msg = "O cliente registou-se";
                oos.writeObject(new RegisterResponse(true, msg, null));
                System.out.println(msg);
                return true;
            }

            msg = "O email ou nome já se econtram registados";
            oos.writeObject(new RegisterResponse(false, msg, "unique"));
            System.out.println(msg);

            statement.close();
            result.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized void addService(Request request, ObjectOutputStream oos) {
        Service service = (Service) request.getRequest();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Statement statement = connection.createStatement();
            /*ResultSet services = statement.executeQuery(
                    "SELECT data_inicio, data_fim FROM servico WHERE strftime('%s','" + dateFormat.format(service.getDateStart()) + "') BETWEEN " +
                            " strftime('%s', data_inicio)" +
                            " AND strftime('%s', data_fim) " +
                            " AND  strftime('%s', '" + dateFormat.format(service.getDateEnd()) + "') BETWEEN strftime('%s', data_inicio)" +
                            " AND strftime('%s', data_fim)"
            );
*/
            //if (!services.next()) {
            ResultSet idSet = statement.executeQuery("SELECT max(id) from servico");
            int id = idSet.getInt(1) + 1;
            idSet.close();

            statement.executeUpdate("INSERT INTO servico(id,id_utilizador,id_funcionario, data_inicio, data_fim, nome_cliente)"
                    + "VALUES("
                    + "'" + id + "',"
                    + "'" + service.getId() + "',"
                    + "'" + service.getIdWorker() + "',"
                    + "'" + dateFormat.format(service.getDateStart()) + "',"
                    + "'" + dateFormat.format(service.getDateEnd()) + "',"
                    + "'" + service.getClientName() + "');");

            for (TypeService typeService : service.getType()) {
                statement.executeUpdate("INSERT INTO reserva_servico (" +
                        " tipo_servico," +
                        " id_servico)" +
                        " VALUES (" +
                        "'+" + typeService.getId() + "'," +
                        "'" + id + "');");
            }

            oos.writeObject(new AddServiceResponse(true, "Success"));
            statement.close();
            return;
            //}

            //oos.writeObject(new AddServiceResponse(false, "Fail"));


        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


    public synchronized void editService(Request request, ObjectOutputStream oos) {
        Pair<Service, Integer> info = (Pair<Service, Integer>) request.getRequest();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Service service = info.getKey();
        int idService = info.getValue();
        System.out.println(dateFormat.format(service.getDateStart()));
        System.out.println(dateFormat.format(service.getDateEnd()));
        System.out.println(dateFormat.format(service.getId()));
        System.out.println(dateFormat.format(service.getIdWorker()));
        try {
            Statement statement = connection.createStatement();
           /* ResultSet services = statement.executeQuery(
                    "SELECT data_inicio, data_fim FROM servico WHERE strftime('%s','"+  dateFormat.format(service.getDateStart()) + "') BETWEEN " +
                            " strftime('%s', data_inicio)" +
                            " AND strftime('%s', data_fim) " +
                            " AND  strftime('%s', '"+ dateFormat.format(service.getDateEnd()) + "') BETWEEN strftime('%s', data_inicio)" +
                            " AND strftime('%s', data_fim)"
            );

            if (!services.next()) {
                ResultSet idSet = statement.executeQuery("SELECT max(id) from servico");
                int id = idSet.getInt(1) + 1;
                idSet.close();*/

            statement.executeUpdate("UPDATE servico SET " +
                    "id_utilizador ='" + service.getId() + "'," +
                    "id_funcionario = '" + service.getIdWorker() + "'," +
                    "data_inicio = '" + dateFormat.format(service.getDateStart()) + "'," +
                    "data_fim = '" + dateFormat.format(service.getDateEnd()) + "'," +
                    "nome_cliente = '" + service.getClientName() + "'WHERE id = '" + idService + "';");

            statement.close();

            oos.writeObject(new EditServiceResponse(true, "Success"));

            //}
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(new EditServiceResponse(false, "Fail"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void removeService(Request request, ObjectOutputStream oos) {
        int serviceId = (int) request.getRequest();

        try {
            Statement statement = connection.createStatement();

            statement.executeUpdate("DELETE FROM servico where id = '" + serviceId + "'");
            String queryReservaServico ="DELETE FROM reserva_servico where id_servico = '" + serviceId + "'";
            statement.executeUpdate(queryReservaServico);
            statement.close();

            System.out.println("Service removed");
            oos.writeObject(new RemoveServiceResponse(true, "Success"));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos.writeObject(new RemoveServiceResponse(false, "False"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized void concludeService(Request request, ObjectOutputStream oos) {
        int serviceId = (int) request.getRequest();

        try {
            Statement statement = connection.createStatement();

            if(statement.executeUpdate("UPDATE servico set concluido = 1 where id = '" + serviceId + "'") != 0) {
                System.out.println("Service concluded");
                oos.writeObject(new ConcludeServiceResponse(true, "Success"));
            }
            else {
                System.out.println("Service was not concluded");
                oos.writeObject(new ConcludeServiceResponse(false, "False"));
            }
            statement.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void getServiceList(ObjectOutputStream oos) {
        //used to update client service lists
        ArrayList<Service> serviceList = new ArrayList<>();
            /*
        try {
            Statement statement = connection.createStatement();
            ResultSet services = statement.executeQuery(
                    "SELECT * FROM servico;"
            );

            while (services.next()) {/*serviceList.add(new Service(services.getInt("id"),
                        services.getInt("id_utilizador"),
                        services.getInt("id_funcionario"),
                        services.getString(4),
                        services.getDate(5),
                        services.getDate(6),
                        services.getString(7)));
                serviceList.add(new Service(services.getInt("id"),
                        services.getInt("id_utilizador"),
                        services.getInt("id_funcionario"),
                        "nome",
                        services.getDate("data_inicio"),
                        services.getDate("data_fim"),
                        "tipo"));

            }    /*
            /*
            System.out.println("Lista de serviços mandada");

            oos.writeObject(new UpdateWeeklyResponse(serviceList));

            statement.close();
            services.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        */
    }

    public synchronized void updateCalendar(Request request, ObjectOutputStream oos) {
        try {
            Date date = (Date) request.getRequest();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            List<Pair<CellType, Boolean>> c = new ArrayList<>();
            Statement statement = connection.createStatement();

            cal.setTime(date);
            cal.add(Calendar.DATE, -1);
            cal.set(Calendar.HOUR_OF_DAY,15);          // end of time where last service available day becomes unavailable after this time
            Calendar today = Calendar.getInstance();


            for (int i = 0; i < 35; i++) {
                cal.add(Calendar.DATE, 1);

                ResultSet resultHours = statement.executeQuery("SELECT data_inicio,data_fim from servico WHERE data_inicio LIKE '%"
                        + dateFormat.format(cal.getTime()) + "%' and id_funcionario in (SELECT id from utilizador where funcionario = 1)");

                long hours = 0;

                while (resultHours.next()) {
                    Date dataInicio = resultHours.getDate("data_inicio");
                    Date dataFim = resultHours.getDate("data_fim");

                    long differenceInTime = dataFim.getTime() - dataInicio.getTime();


                    hours += (differenceInTime / (1000 * 60 * 60)) % 24;
                }
                if (!cal.before(today)) {
                    if (hours == 8) {
                        ResultSet resultSetDayOff = statement.executeQuery("SELECT id from folga WHERE dia LIKE '%"
                                + dateFormat.format(cal.getTime()) + "%' and id_funcionario in (SELECT id from utilizador where funcionario = 1)");

                        if (resultSetDayOff.next()) {
                            c.add(new Pair<>(CellType.DISABLE, true));
                        } else
                            c.add(new Pair<>(CellType.AVAILABLE, true));
                    } else {
                        ResultSet resultSetDayOff = statement.executeQuery("SELECT id from folga WHERE dia LIKE '%"
                                + dateFormat.format(cal.getTime()) + "%' and id_funcionario in (SELECT id from utilizador where funcionario = 1)");
                        if (resultSetDayOff.next()) {
                            c.add(new Pair<>(CellType.DISABLE, false));
                        } else
                            c.add(new Pair<>(CellType.AVAILABLE, false));
                    }
                } else {
                    c.add(new Pair<>(CellType.DISABLE, false));
                }
            }
            oos.writeObject(new UpdateCalenderResponse(c));
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void updateWeaklyCalendar(Request request, ObjectOutputStream oos) {
        try {
            Statement statement = connection.createStatement();

            Pair<User, Date> info = (Pair<User, Date>) request.getRequest();

            int idWorker = info.getKey().getId();

            Date date = info.getValue();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            int STAR_HOUR = 8;
            int AMOUNT_OF_HOURS_OF_WORK = 8;
            int DAYS_OF_WEEK = 7;

            CellService[][] cellServices = new CellService[AMOUNT_OF_HOURS_OF_WORK][DAYS_OF_WEEK];
            calendar.add(Calendar.DATE, -1);


            for (int day = 0; day < DAYS_OF_WEEK; day++) {
                calendar.set(Calendar.HOUR_OF_DAY, STAR_HOUR);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.add(Calendar.DATE, 1);
                for (int hour = 0; hour < 8; hour++) {
                    String cellInitial = dateFormat.format(calendar.getTime());
                    calendar.add(Calendar.HOUR_OF_DAY, 1);
                    String cellEnd = dateFormat.format(calendar.getTime());
                    ResultSet resultSet = statement.executeQuery("SELECT t.*,tr.nome,tipo.nome as tipo_nome,tipo.duracao as tipo_duracao FROM servico AS t" +
                            "   INNER JOIN utilizador AS tr ON t.id_funcionario = tr.id" +
                            "   INNER JOIN reserva_servico AS reserva ON t.id = reserva.id_servico" +
                            "   INNER JOIN tipo_servico AS tipo ON tipo.id = reserva.tipo_servico " +
                            "WHERE strftime('%s','" + cellInitial + "') BETWEEN " +
                            "strftime('%s', data_inicio)" +
                            " AND strftime('%s', data_fim)" +
                            " AND  strftime('%s', '" + cellEnd + "') BETWEEN strftime('%s', data_inicio)" +
                            " AND strftime('%s', data_fim) and tr.id=" + idWorker);

                    List<String> typeOfServices = new ArrayList<>();
                    int duration = 0;
                    int id = 0;
                    int idUtilizador = 0;
                    String initialDate = "";
                    String endDate = "";
                    int idFuncionario = 0;
                    String nomeFuncionario = null;
                    int isConcluded = 0;


                    while (resultSet.next()) {
                        id = resultSet.getInt("id");
                        idUtilizador = resultSet.getInt("id_utilizador");
                        initialDate = resultSet.getString("data_inicio");
                        endDate = resultSet.getString("data_fim");
                        idFuncionario = resultSet.getInt("id_funcionario");
                        nomeFuncionario = resultSet.getString("nome");
                        typeOfServices.add(resultSet.getString("tipo_nome"));
                        duration += resultSet.getInt("tipo_duracao");
                        isConcluded = resultSet.getInt("concluido");
                    }
                    Calendar today = Calendar.getInstance();
                    if(calendar.before(today))
                        cellServices[hour][day] = new CellService(id, CellType.DISABLE);
                    else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                        cellServices[hour][day] = new CellService(id, CellType.DISABLE);
                    }
                    else {
                        if (id != 0) {
                            cellServices[hour][day] = new CellService(id, idUtilizador, nomeFuncionario, idFuncionario, false, initialDate, endDate, typeOfServices,duration,isConcluded);
                        } else {
                            DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                            ResultSet resultSetDayOff = statement.executeQuery("SELECT id from folga WHERE dia LIKE '%"
                                    + dateFormat2.format(calendar.getTime()) + "%' and id_funcionario = '" + idWorker + "'");
                            if (resultSetDayOff.next()) {
                                cellServices[hour][day] = new CellService(id, CellType.DISABLE);
                            } else {
                                cellServices[hour][day] = new CellService(id, CellType.AVAILABLE);
                            }
                        }
                    }
                }
            }
            oos.writeObject(new UpdateWeeklyCalenderResponse(cellServices));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized boolean disconnect(Request request, ObjectOutputStream oos) {
        try {
            if (request.getId() != -1) {
                Statement statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery("select id from utilizador where autenticado = 1 and id = '" + request.getId() + "';");

                if (resultSet.next()) {
                    int row = statement.executeUpdate("UPDATE utilizador SET autenticado = 0 WHERE id = '" + request.getId() + "';");

                    if (row != 0) {
                        System.out.println("Client with id " + request.getId() + " was disconnect!");
                        statement.close();
                        return true;
                    }
                }

                System.out.println("Client with id " + request.getId() + " is already disconnected!");
                statement.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public synchronized void getWorkers(ObjectOutputStream oos) {
        try {
            List<User> workers = new ArrayList<>();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id,nome from utilizador WHERE funcionario = 1;");

            while (resultSet.next()) {
                workers.add(new User(resultSet.getInt("id"), resultSet.getString("nome")));
            }

            oos.writeObject(new WorkersResponse(workers));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized List<User> getWorkers() {
        try {
            List<User> workers = new ArrayList<>();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id,nome from utilizador WHERE funcionario = 1;");

            while (resultSet.next()) {
                workers.add(new User(resultSet.getInt("id"), resultSet.getString("nome")));
            }

            return workers;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public synchronized void getTypeOfServices(ObjectOutputStream oos) {
        try {
            List<TypeService> typeServices = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * from tipo_servico;");

            while (resultSet.next()) {
                typeServices.add(new TypeService(resultSet.getInt("id"), resultSet.getString("nome"), resultSet.getInt("duracao")));
            }

            oos.writeObject(new UpdateTypeServiceResponse(typeServices));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void updateEmployeeWeeklyCalendar(Request request,ObjectOutputStream oos){
        try {
            Statement statement = connection.createStatement();

            Pair<User, Date> info = (Pair<User, Date>) request.getRequest();

            int idUser = info.getKey().getId();

            Date date = info.getValue();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            int STAR_HOUR = 8;
            int AMOUNT_OF_HOURS_OF_WORK = 8;
            int DAYS_OF_WEEK = 7;

            CellService[][] cellServices = new CellService[AMOUNT_OF_HOURS_OF_WORK][DAYS_OF_WEEK];
            calendar.add(Calendar.DATE, -1);

            for (int day = 0; day < DAYS_OF_WEEK; day++) {
                calendar.set(Calendar.HOUR_OF_DAY, STAR_HOUR);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.add(Calendar.DATE, 1);
                for (int hour = 0; hour < 8; hour++) {
                    String cellInitial = dateFormat.format(calendar.getTime());
                    calendar.add(Calendar.HOUR_OF_DAY, 1);
                    String cellEnd = dateFormat.format(calendar.getTime());
                    ResultSet resultSet = statement.executeQuery("SELECT t.*,tr.nome,tipo.nome as tipo_nome,tipo.duracao as tipo_duracao FROM servico AS t" +
                            "   INNER JOIN utilizador AS tr ON t.id_utilizador = tr.id" +
                            "   INNER JOIN reserva_servico AS reserva ON t.id = reserva.id_servico" +
                            "   INNER JOIN tipo_servico AS tipo ON tipo.id = reserva.tipo_servico " +
                            "WHERE strftime('%s','" + cellInitial + "') BETWEEN " +
                            "strftime('%s', data_inicio)" +
                            " AND strftime('%s', data_fim)" +
                            " AND  strftime('%s', '" + cellEnd + "') BETWEEN strftime('%s', data_inicio)" +
                            " AND strftime('%s', data_fim) and tr.id=" + idUser +" and funcionario = 1");

                    List<String> typeOfServices = new ArrayList<>();
                    int duration = 0;
                    int id = 0;
                    int idUtilizador = 0;
                    String initialDate = "";
                    String endDate = "";
                    int idFuncionario = 0;
                    String nomeFuncionario = null;

                    while (resultSet.next()) {
                        id = resultSet.getInt("id");
                        idUtilizador = resultSet.getInt("id_utilizador");
                        initialDate = resultSet.getString("data_inicio");
                        endDate = resultSet.getString("data_fim");
                        idFuncionario = resultSet.getInt("id_funcionario");
                        nomeFuncionario = resultSet.getString("nome");
                        typeOfServices.add(resultSet.getString("tipo_nome"));
                        duration += resultSet.getInt("tipo_duracao");
                    }

                    if (id != 0) {
                        cellServices[hour][day] = new CellService(id, idUtilizador, nomeFuncionario, idFuncionario, false, initialDate, endDate, typeOfServices, duration,0);
                    } else {
                        cellServices[hour][day] = null;

                    }
                }
            }
            oos.writeObject(new UpdateUserWeeklyCalendarResponse(cellServices));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void updateUserWeeklyCalendar(Request request, ObjectOutputStream oos) {
        try {

            Statement statement = connection.createStatement();

            Pair<User, Date> info = (Pair<User, Date>) request.getRequest();

            int idUser = info.getKey().getId();

            Date date = info.getValue();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            int STAR_HOUR = 8;
            int AMOUNT_OF_HOURS_OF_WORK = 8;
            int DAYS_OF_WEEK = 7;

            CellService[][] cellServices = new CellService[AMOUNT_OF_HOURS_OF_WORK][DAYS_OF_WEEK];
            calendar.add(Calendar.DATE, -1);

            for (int day = 0; day < DAYS_OF_WEEK; day++) {
                calendar.set(Calendar.HOUR_OF_DAY, STAR_HOUR);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.add(Calendar.DATE, 1);
                for (int hour = 0; hour < 8; hour++) {
                    String cellInitial = dateFormat.format(calendar.getTime());
                    calendar.add(Calendar.HOUR_OF_DAY, 1);
                    String cellEnd = dateFormat.format(calendar.getTime());
                    ResultSet resultSet = statement.executeQuery("SELECT t.*,tr.nome,tipo.nome as tipo_nome,tipo.duracao as tipo_duracao FROM servico AS t" +
                            "   INNER JOIN utilizador AS tr ON t.id_utilizador = tr.id" +
                            "   INNER JOIN reserva_servico AS reserva ON t.id = reserva.id_servico" +
                            "   INNER JOIN tipo_servico AS tipo ON tipo.id = reserva.tipo_servico " +
                            "WHERE strftime('%s','" + cellInitial + "') BETWEEN " +
                            "strftime('%s', data_inicio)" +
                            " AND strftime('%s', data_fim)" +
                            " AND  strftime('%s', '" + cellEnd + "') BETWEEN strftime('%s', data_inicio)" +
                            " AND strftime('%s', data_fim) and tr.id=" + idUser);

                    List<String> typeOfServices = new ArrayList<>();
                    int duration = 0;
                    int id = 0;
                    int idUtilizador = 0;
                    String initialDate = "";
                    String endDate = "";
                    int idFuncionario = 0;
                    String nomeFuncionario = null;

                    while (resultSet.next()) {
                        id = resultSet.getInt("id");
                        idUtilizador = resultSet.getInt("id_utilizador");
                        initialDate = resultSet.getString("data_inicio");
                        endDate = resultSet.getString("data_fim");
                        idFuncionario = resultSet.getInt("id_funcionario");
                        nomeFuncionario = resultSet.getString("nome");
                        typeOfServices.add(resultSet.getString("tipo_nome"));
                        duration += resultSet.getInt("tipo_duracao");
                    }

                    if (id != 0) {
                        cellServices[hour][day] = new CellService(id, idUtilizador, nomeFuncionario, idFuncionario, false, initialDate, endDate, typeOfServices, duration,0);
                    } else {
                        cellServices[hour][day] = null;

                    }
                }
            }
            oos.writeObject(new UpdateUserWeeklyCalendarResponse(cellServices));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Pair<Boolean, String> removeWorker(User user) {
        try {
            Statement statement = connection.createStatement();
            ResultSet isFuncionario =  statement.executeQuery("SELECT funcionario from utilizador where id = '" + user.getId() + "';");
            if (isFuncionario.getInt(1) == 1) {
                ResultSet resultSet = statement.executeQuery("SELECT id from servico where id_funcionario = '" + user.getId() + "';");
                if (!resultSet.next()) {
                    System.out.println("Funcionário removido com sucesso");
                    statement.executeUpdate("DELETE from utilizador where id ='" + user.getId() + "';");
                    return new Pair<>(true, "O funcionário foi removido com sucesso");
                }
            } else {
                return new Pair<>(false, "Não pode remover clientes apenas funcionários");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Pair<>(false, "O funcionário não foi removido\nPois já esta associado a um serviço");
    }

    public Map<String, Pair<Integer, Integer>> getStatistic(User user) {
        try {
            //Data -  nao concluidos - concluidos
            Map<String, Pair<Integer, Integer>> map = new HashMap<>();
            String data = "";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT data_inicio,concluido from servico where id_funcionario = '"
                    + user.getId() + "' order by data_inicio;");

            int concluido = 0;
            int naoConcluido = 0;
            while (resultSet.next()) {
                String dataInicio = resultSet.getString("data_inicio");
                int rConcluido = resultSet.getInt("concluido");
                if (data.isEmpty()) {
                    data = dataInicio;
                }
                // is empty or 2022-10-12 || ignore hh:mm:ss
                else if (!data.contains(dataInicio.substring(0, 10))) {
                    map.put(dataInicio, new Pair<>(naoConcluido, concluido));
                    data = dataInicio;
                    concluido = 0;
                    naoConcluido = 0;
                }

                if (rConcluido == 0) {
                    naoConcluido++;
                } else {
                    concluido++;
                }
            }
            return map;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Pair<Boolean, String> handleDay(User user, Date date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String dia = format.format(date);

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id from servico where id_funcionario =" + user.getId() + " and data_inicio='%" + dia + "%';");
            if (!resultSet.next()) {
                ResultSet idSet = statement.executeQuery("SELECT id from folga where id_funcionario ='" + user.getId() + "' and dia='" + dia + "';");
                if (!idSet.next()) {
                    statement.executeUpdate("INSERT into folga (id_funcionario,dia) VALUES('" + user.getId() + "','" + dia + "');");
                    return new Pair<>(true, "O dia foi desativado com sucesso");
                } else {
                    statement.executeUpdate("DELETE from folga where id= '" + resultSet.getInt(1) + "';");
                    return new Pair<>(true, "O dia foi reativado com sucesso");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Pair<>(false, "Não foi possivel desativar o dia pois o funcionário já tem reservas");
    }

    public List<CellType> updateCalendarManager(User user, Date date) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            List<CellType> c = new ArrayList<>();
            Statement statement = connection.createStatement();

            cal.setTime(date);
            cal.add(Calendar.DATE, -1);

            for (int i = 0; i < 35; i++) {
                cal.add(Calendar.DATE, 1);
                ResultSet resultHours = statement.executeQuery("SELECT data_inicio,data_fim from servico WHERE data_inicio LIKE '%"
                        + dateFormat.format(cal.getTime()) + "%' and id_funcionario = '" + user.getId() + "'");

                if (resultHours.next()) {
                    long hours = 0;

                    while (resultHours.next()) {
                        Date dataInicio = resultHours.getDate("data_inicio");
                        Date dataFim = resultHours.getDate("data_fim");

                        long differenceInTime= dataFim.getTime() - dataInicio.getTime();


                        hours += (differenceInTime / (1000 * 60 * 60)) % 24;
                    }

                    if (hours == 8) {
                        c.add(CellType.AVAILABLE);
                        System.out.println("One day is full");
                    } else {
                        c.add(CellType.FULL);
                    }
                } else {

                    ResultSet resultSet = statement.executeQuery("SELECT id from folga WHERE dia LIKE '%"
                            + dateFormat.format(cal.getTime()) + "%' and id_funcionario = '" + user.getId() + "'");

                    if (resultSet.next()) {
                        c.add(CellType.DISABLE);
                        System.out.println("One day is disabled");
                    } else {
                        c.add(CellType.AVAILABLE);
                    }
                }
            }

            return c;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateCalendarWorker(Request request, ObjectOutputStream oos) {
        Pair<User, Date> pair = (Pair<User, Date>) request.getRequest();
        User user = pair.getKey();
        Date date = pair.getValue();

        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            List<Pair<CellType, Boolean>> c = new ArrayList<>();
            Statement statement = connection.createStatement();

            cal.setTime(date);
            cal.add(Calendar.DATE, -1);
            for (int i = 0; i < 35; i++) {
                cal.add(Calendar.DATE, 1);

                ResultSet resultHours = statement.executeQuery("SELECT data_inicio,data_fim from servico WHERE data_inicio LIKE '%"
                        + dateFormat.format(cal.getTime()) + "%' and id_funcionario in (SELECT id from utilizador where funcionario = 1)");

                long hours = 0;

                while (resultHours.next()) {
                    Date dataInicio = resultHours.getDate("data_inicio");
                    Date dataFim = resultHours.getDate("data_fim");

                    long differenceInTime = dataFim.getTime() - dataInicio.getTime();


                    hours += (differenceInTime / (1000 * 60 * 60)) % 24;
                }
                Calendar today = Calendar.getInstance();
                if (!cal.before(today)) {
                    if (hours == 8) {
                        ResultSet resultSetDayOff = statement.executeQuery("SELECT id from folga WHERE dia LIKE '%"
                                + dateFormat.format(cal.getTime()) + "%' and id_funcionario = '" + user.getId() + "'");

                        if (resultSetDayOff.next()) {
                            c.add(new Pair<>(CellType.DISABLE, true));
                        } else
                            c.add(new Pair<>(CellType.AVAILABLE, true));
                    } else {
                        ResultSet resultSetDayOff = statement.executeQuery("SELECT id from folga WHERE dia LIKE '%"
                                + dateFormat.format(cal.getTime()) + "%' and id_funcionario = '" + user.getId() + "'");
                        if (resultSetDayOff.next()) {
                            c.add(new Pair<>(CellType.DISABLE, false));
                        } else
                            c.add(new Pair<>(CellType.AVAILABLE, false));
                    }
                } else {
                    c.add(new Pair<>(CellType.DISABLE, false));
                }
            }
            oos.writeObject(new UpdateCalenderResponse(c));
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}