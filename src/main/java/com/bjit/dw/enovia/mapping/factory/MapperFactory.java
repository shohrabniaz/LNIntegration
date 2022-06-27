package com.bjit.dw.enovia.mapping.factory;


import com.bjit.dw.enovia.mapping.processors.IMapper;

/**
 *
 * @author BJIT
 */
public class MapperFactory {

    static final org.apache.log4j.Logger MAPPER_FACTORY_LOGGER = org.apache.log4j.Logger.getLogger(MapperFactory.class.getName());

    public static <T extends IMapper, K> T getInstnace(Class<T> type, Class<K> mapObject) throws Exception {
        try {
            try {
                MAPPER_FACTORY_LOGGER.info("Instantiating " + type.getCanonicalName());
                final T newInstance = type.newInstance();
                newInstance.__init__(mapObject);
                return newInstance;
            } catch (Exception exp) {
//                exp.printStackTrace(System.out);
                MAPPER_FACTORY_LOGGER.error("Initialization of " + type.getCanonicalName() + " has been failed");
                MAPPER_FACTORY_LOGGER.error("Error occured : " + exp.getMessage());
                throw exp;
            }
        } catch (IllegalAccessException | InstantiationException exp) {
//            exp.printStackTrace(System.out);
            MAPPER_FACTORY_LOGGER.error("Initialization of " + type.getCanonicalName() + " has been failed");
            MAPPER_FACTORY_LOGGER.error("Error occured : " + exp.getMessage());
            throw exp;
        }
    }
}
