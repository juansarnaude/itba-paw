package ar.edu.itba.paw.persistence;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StatusDaoImpl implements StatusDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<String> getAllStatus() {
        String sql = "SELECT m.status " +
                "FROM Media m " +
                "GROUP BY m.status " +
                "ORDER BY COUNT(m) DESC";

        return entityManager.createQuery(sql).getResultList();
    }

    @Override
    public List<String> getAllStatus(int type) {
        ArrayList<String> argtype = new ArrayList<>();
        ArrayList<Object> args = new ArrayList<>();

        String sql = "SELECT m.status FROM Media m ";

        if (type == 0 || type == 1) {
            sql += " WHERE m.type = :type ";
            argtype.add("type");
            args.add(type == 1);
        } else {
            sql += " WHERE m.type IS NOT NULL ";
        }

        sql+= "GROUP BY m.status ORDER BY COUNT(m) DESC";

        Query query = entityManager.createQuery(sql);
        for (int i = 0; i < args.size(); i++) {
            query.setParameter(argtype.get(i), args.get(i));
        }
        return query.getResultList();
    }
}
