/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bjit.project_structure.utilities;

import com.bjit.project_structure.model.ActivityModel;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.MqlUtil;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import matrix.db.BusinessObject;
import matrix.db.Context;
import matrix.db.ExpansionWithSelect;
import matrix.db.RelationshipType;
import matrix.db.RelationshipWithSelect;
import matrix.db.RelationshipWithSelectList;
import matrix.util.MatrixException;
import matrix.util.StringList;

/**
 *
 * @author BJIT
 */
public class ProjectSpaceUtil {

    private static final org.apache.log4j.Logger PROJECT_SPACE_UTIL_LOGGER = org.apache.log4j.Logger.getLogger(ProjectSpaceUtil.class);

    public HashMap<String, ActivityModel> expandTask(Context context, String projectSpaceId) throws Exception {

        HashMap<String, ActivityModel> childParentHashMap = new HashMap<>();

        StringList objSelect = new StringList();
        objSelect.add("type");
        objSelect.add("name");
        objSelect.add("revision");
        objSelect.add("id");
        StringList relSelect = new StringList();
        relSelect.add("name");

        BusinessObject projectSpaceBusinessObject = new BusinessObject(projectSpaceId);
        projectSpaceBusinessObject.open(context);
        ExpansionWithSelect expandSelect = projectSpaceBusinessObject.expandSelect(context, "Subtask", "Task,Milestone", objSelect, relSelect, false, true, (short) 99);

        RelationshipWithSelectList relationships = expandSelect.getRelationships();

        relationships.getIterator().forEach((RelationshipWithSelect relationshipWithSelect) -> {
            try {
                relationshipWithSelect.open(context);

                BusinessObject to = relationshipWithSelect.getTo();
                BusinessObject from = relationshipWithSelect.getFrom();
                to.open(context);
                from.open(context);

                PROJECT_SPACE_UTIL_LOGGER.info("Parent Name : " + from.getName());
                PROJECT_SPACE_UTIL_LOGGER.info("Object Name : " + to.getName());
                PROJECT_SPACE_UTIL_LOGGER.info("Object Type : " + to.getTypeName());
                PROJECT_SPACE_UTIL_LOGGER.info("Object Revision : " + to.getRevision());
                PROJECT_SPACE_UTIL_LOGGER.info("Object Id : " + to.getObjectId());

                PROJECT_SPACE_UTIL_LOGGER.info("Relationship Name : " + relationshipWithSelect.getTypeName());
                PROJECT_SPACE_UTIL_LOGGER.info("Level : " + relationshipWithSelect.getLevel());

                String relationshipId = relationshipWithSelect.getName();
                RelationshipType relationshipType = relationshipWithSelect.getRelationshipType();
                String relationshipTypeName = relationshipType.getName();

                ActivityModel activityModel = new ActivityModel();
                activityModel.setActivityId(to.getObjectId());
                activityModel.setActivityName(to.getName());
                activityModel.setActivityType(to.getTypeName());
                activityModel.setParentName(from.getName());
                activityModel.setParentId(from.getObjectId());
                activityModel.setRelationShipName(relationshipWithSelect.getTypeName());
                activityModel.setRelationshipId(relationshipId);

                childParentHashMap.put(to.getName(), activityModel);
                relationshipWithSelect.close(context);
            } catch (MatrixException ex) {
                PROJECT_SPACE_UTIL_LOGGER.error(ex);
            }
        });

        return childParentHashMap;
    }

    public HashMap<String, ActivityModel> expandMilestone(Context context, String projectSpaceId) throws Exception {

        HashMap<String, ActivityModel> childParentHashMap = new HashMap<>();

        StringList objSelect = new StringList();
        objSelect.add("type");
        objSelect.add("name");
        objSelect.add("revision");
        objSelect.add("id");
        StringList relSelect = new StringList();
        relSelect.add("name");

        BusinessObject projectSpaceBusinessObject = new BusinessObject(projectSpaceId);
        projectSpaceBusinessObject.open(context);
        ExpansionWithSelect expandSelect = projectSpaceBusinessObject.expandSelect(context, "Subtask", "Task,Milestone", objSelect, relSelect, false, true, (short) 99);

        RelationshipWithSelectList relationships = expandSelect.getRelationships();

        relationships.getIterator().forEach((RelationshipWithSelect relationshipWithSelect) -> {
            try {
                relationshipWithSelect.open(context);

                BusinessObject to = relationshipWithSelect.getTo();
                BusinessObject from = relationshipWithSelect.getFrom();
                to.open(context);
                from.open(context);

                PROJECT_SPACE_UTIL_LOGGER.info("Parent Name : " + from.getName());
                PROJECT_SPACE_UTIL_LOGGER.info("Object Name : " + to.getName());
                PROJECT_SPACE_UTIL_LOGGER.info("Object Type : " + to.getTypeName());
                PROJECT_SPACE_UTIL_LOGGER.info("Object Revision : " + to.getRevision());
                PROJECT_SPACE_UTIL_LOGGER.info("Object Id : " + to.getObjectId());

                PROJECT_SPACE_UTIL_LOGGER.info("Relationship Name : " + relationshipWithSelect.getTypeName());
                PROJECT_SPACE_UTIL_LOGGER.info("Level : " + relationshipWithSelect.getLevel());

                ActivityModel activityModel = new ActivityModel();
                activityModel.setActivityId(to.getObjectId());
                activityModel.setActivityName(to.getName());
                activityModel.setActivityType(to.getTypeName());
                activityModel.setParentName(from.getName());
                activityModel.setParentId(from.getObjectId());
                activityModel.setRelationShipName(relationshipWithSelect.getTypeName());

                childParentHashMap.put(to.getName(), activityModel);

            } catch (MatrixException ex) {
                PROJECT_SPACE_UTIL_LOGGER.error(ex);
            }
        });

        return childParentHashMap;
    }

    public Boolean updateObject(Context context, String objectId, String propertyName, String propertyValue) throws FrameworkException {

        try {
            PROJECT_SPACE_UTIL_LOGGER.info("Property Name : " + propertyName);
            PROJECT_SPACE_UTIL_LOGGER.info("Property Value : " + propertyValue);

            String updateQuery = "mod bus " + objectId + " \"" + propertyName + "\" \"" + propertyValue + "\"";
            PROJECT_SPACE_UTIL_LOGGER.debug("Modify Query : " + updateQuery);

            String mqlCommand = MqlUtil.mqlCommand(context, updateQuery);
            PROJECT_SPACE_UTIL_LOGGER.info("Returned Result : " + mqlCommand);

            return true;
        } catch (FrameworkException exp) {
            PROJECT_SPACE_UTIL_LOGGER.error(exp.getMessage());
            throw exp;
        } catch (NullPointerException exp) {
            PROJECT_SPACE_UTIL_LOGGER.error(exp.getMessage());
            throw exp;
        } catch (Exception exp) {
            PROJECT_SPACE_UTIL_LOGGER.error(exp.getMessage());
            throw exp;
        }
    }

    public Boolean updateItem(Context context, String objectId, HashMap<String, String> propertyMap) throws FrameworkException {
        String errorMessage;

        try {
            if (propertyMap.isEmpty()) {
                errorMessage = "Attribute or Property Map is Empty";
                PROJECT_SPACE_UTIL_LOGGER.error(errorMessage);
                throw new NullPointerException(errorMessage);
            }

            PROJECT_SPACE_UTIL_LOGGER.info("Attribute or Property Map : " + propertyMap);

            StringBuilder modQueryBuilder = new StringBuilder();
            modQueryBuilder.append("modify bus ").append(objectId).append(" ");

            propertyMap.forEach((String key, String value) -> {
                modQueryBuilder.append("\"").append(key).append("\"").append(" ").append("\"").append(value).append("\" ");
            });

            String updateQuery = modQueryBuilder.toString();
            PROJECT_SPACE_UTIL_LOGGER.debug("Modify Query : " + updateQuery);

            String queryResult = MqlUtil.mqlCommand(context, updateQuery);
            PROJECT_SPACE_UTIL_LOGGER.debug("Returned Result : " + queryResult);

            checkWarningInExecutingQuery(queryResult);

            return true;
        } catch (FrameworkException exp) {
            PROJECT_SPACE_UTIL_LOGGER.error(exp.getMessage());
            throw exp;
        } catch (NullPointerException exp) {
            PROJECT_SPACE_UTIL_LOGGER.error(exp.getMessage());
            throw exp;
        } catch (Exception exp) {
            PROJECT_SPACE_UTIL_LOGGER.error(exp.getMessage());
            throw exp;
        }
    }

    private static synchronized void checkWarningInExecutingQuery(String queryResult) {
        String warningPattern = "Warning: #1900075:|Warning: #1500218:|Business object has no attribute|at end of command ignored";

        Pattern pattern = Pattern.compile(warningPattern);
        Matcher matcher = pattern.matcher(queryResult);
        if (matcher.find()) {
            PROJECT_SPACE_UTIL_LOGGER.error(queryResult);
            throw new RuntimeException(queryResult);
        }
    }
    
    public void deleteTaskAndMileStone(Context context, ActivityModel taskModel) {
        PROJECT_SPACE_UTIL_LOGGER.info("#################");
        PROJECT_SPACE_UTIL_LOGGER.info("# Delete A TASK #");
        PROJECT_SPACE_UTIL_LOGGER.info("#################");

        PROJECT_SPACE_UTIL_LOGGER.debug("Task Name : '" + taskModel.getActivityName() + "'");

        String activityId = taskModel.getActivityId();
        PROJECT_SPACE_UTIL_LOGGER.debug("Task Id : '" + activityId + "'");
        try {
            String deleteQuery = "delete bus " + activityId;
            PROJECT_SPACE_UTIL_LOGGER.info("Delete query : '" + deleteQuery + "'\n\n\n");

            String mqlCommand = MqlUtil.mqlCommand(context, deleteQuery);
            PROJECT_SPACE_UTIL_LOGGER.info("Returned response : " + mqlCommand);
        } catch (Exception ex) {
            PROJECT_SPACE_UTIL_LOGGER.warn(ex);
        }
    }

    public void deleteTaskAndMileStone(Context context, HashMap<String, ActivityModel> projectSpaceExpandedMap) {
        projectSpaceExpandedMap.forEach((String key, ActivityModel value) -> {
            PROJECT_SPACE_UTIL_LOGGER.info("#################");
            PROJECT_SPACE_UTIL_LOGGER.info("# Delete A TASK #");
            PROJECT_SPACE_UTIL_LOGGER.info("#################");

            PROJECT_SPACE_UTIL_LOGGER.debug("Task Name : '" + key + "'");
            ActivityModel taskModel = projectSpaceExpandedMap.get(key);
            String activityId = taskModel.getActivityId();
            PROJECT_SPACE_UTIL_LOGGER.debug("Task Id : '" + activityId + "'");
            try {
                String deleteQuery = "delete bus " + activityId;
                PROJECT_SPACE_UTIL_LOGGER.debug("Delete query : '" + deleteQuery + "'\n\n\n");

                String mqlCommand = MqlUtil.mqlCommand(context, deleteQuery);
            } catch (Exception ex) {
                PROJECT_SPACE_UTIL_LOGGER.warn(ex);
            }
        });
    }
}
