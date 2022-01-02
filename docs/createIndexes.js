db.sensorSettings.createIndex({'sensorName':1}, {unique: true, sparse: true});
db.temperatureMeasurement.createIndex({"createdOn": 1, "sensor": 1}, {unique: true, sparse: true});