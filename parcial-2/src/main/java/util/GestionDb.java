package util;



import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;
import org.postgresql.ds.PGSimpleDataSource;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by vacax on 03/06/16.
 */
public class GestionDb<T> {

    private static EntityManagerFactory emf;
    private Class<T> claseEntidad;


    public GestionDb(Class<T> claseEntidad) {
        if(emf == null) {

            emf = Persistence.createEntityManagerFactory("UnidadPersistencia");

        }
        this.claseEntidad = claseEntidad;

    }

    public EntityManager getEntityManager(){
        return emf.createEntityManager();
    }


    /**
     * Configurar información de la conexión de Heroku.
     * Tomado de https://gist.github.com/mlecoutre/4088178
     * @return
     */
    private EntityManagerFactory getConfiguracionBaseDatosHeroku(){
        //Leyendo la información de la variable de ambiente de Heroku
        String databaseUrl = System.getenv("DATABASE_URL");
        StringTokenizer st = new StringTokenizer(databaseUrl, ":@/");
        //Separando las información del conexión.
        String dbVendor = st.nextToken();
        String userName = st.nextToken();
        String password = st.nextToken();
        String host = st.nextToken();
        String port = st.nextToken();
        String databaseName = st.nextToken();
        //creando la jbdc String
        String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s?sslmode=require", host, port, databaseName);
        //pasando las propiedades.
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", jdbcUrl );
        properties.put("javax.persistence.jdbc.user", userName );
        properties.put("javax.persistence.jdbc.password", password );
        //
        return Persistence.createEntityManagerFactory("Heroku", properties);
    }

    /**
     * Metodo para obtener el valor del campo anotado como @ID.
     * @param entidad
     * @return
     */
    private Object getValorCampo(T entidad){
        if(entidad == null){
            return null;
        }
        //aplicando la clase de reflexión.
        for(Field f : entidad.getClass().getDeclaredFields()) {  //tomando todos los campos privados.
            if (f.isAnnotationPresent(Id.class)) { //preguntando por la anotación ID.
                try {
                    f.setAccessible(true);
                    Object valorCampo = f.get(entidad);

                    System.out.println("Nombre del campo: "+f.getName());
                    System.out.println("Tipo del campo: "+f.getType().getName());
                    System.out.println("Valor del campo: "+valorCampo );

                    return valorCampo;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     *
     * @param entidad
     */
    public T crear(T entidad) throws IllegalArgumentException, EntityExistsException, PersistenceException{
        EntityManager em = getEntityManager();

        try {

            em.getTransaction().begin();
            em.persist(entidad);
            em.getTransaction().commit();

        }finally {
            em.close();
        }
        return entidad;
    }

    /**
     *
     * @param entidad
     */
    public T editar(T entidad) throws PersistenceException{
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            em.merge(entidad);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
        return entidad;
    }

    /**
     *
     * @param entidadId
     */
    public boolean eliminar(Object entidadId) throws PersistenceException{
        boolean ok = false;
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            T entidad = em.find(claseEntidad, entidadId);
            em.remove(entidad);
            em.getTransaction().commit();
            ok = true;
        }finally {
            em.close();
        }
        return ok;
    }

    /**
     *
     * @param id
     * @return
     */
    public T find(Object id) throws PersistenceException {
        EntityManager em = getEntityManager();
        try{
            return em.find(claseEntidad, id);
        } finally {
            em.close();
        }
    }

    /**
     *
     * @return
     */
    public List<T> findAll() throws PersistenceException {
        EntityManager em = getEntityManager();
        try{
            CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(claseEntidad);
            criteriaQuery.select(criteriaQuery.from(claseEntidad));
            return em.createQuery(criteriaQuery).getResultList();
        } finally {
            em.close();
        }
    }


//    //COSAS DE COCKROACHLABS:
//
//
//    //Conexion con Cockroachlabs
//    public static Connection getConnection(){
//        PGSimpleDataSource ds = new PGSimpleDataSource();
//        if(System.getenv("JDBC_DATABASE_URL")!=null) {
//            ds.setURL(System.getenv("JDBC_DATABASE_URL"));
//            try {
//                return ds.getConnection();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }else {
//            System.out.println("No se ha encontrado la variable de entorno JDBC_DATABASE_URL");
//            return null;
//        }
//    }
//
//    public static void initializeDatabase() {
//        String sql = "CREATE TABLE IF NOT EXISTS authentication_log (" +
//                "id SERIAL PRIMARY KEY," +
//                "username VARCHAR(255) NOT NULL," +
//                "login_time TIMESTAMP NOT NULL" +
//                ")";
//
//        try (Connection conn = getConnection();
//             Statement stmt = conn.createStatement()) {
//            stmt.execute(sql);
//        } catch (Exception e) {
//            System.out.println("No se ha encontrado la variable de entorno JDBC_DATABASE_URL");
//        }
//    }
//
//    public static void logAuthentication(String username) {
//        String sql = "INSERT INTO authentication_log (username, login_time) VALUES (?, ?)";
//
//        try (Connection conn = getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setString(1, username);
//            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
//
//            pstmt.executeUpdate();
//        } catch (Exception e) {
//            System.out.println("No se ha encontrado la variable de entorno JDBC_DATABASE_URL");
//        }
//    }


}
