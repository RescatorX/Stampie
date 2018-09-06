package cz.kalina.stampie.data.dao.intf;

import cz.kalina.stampie.data.Database;
import cz.kalina.stampie.data.entities.ExtData;
import cz.kalina.stampie.utils.STPException;

public interface IExtDataDAO extends IGenericDAO<ExtData> {
    public ExtData getItemByType(String type, Database db) throws STPException;
}
