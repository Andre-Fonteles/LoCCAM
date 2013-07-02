package br.ufc.great.syssu.servicemanagement.services;

/*
 * "put": {
 * 		"type":"event",
 * 		"parameters": [
 *	    	{"name":"domain", "type": "string"},
 *	    	{"name": "query", "type": 
 *				{"name": "Query", "properties": 
 *					{"pattern": {"type": "string"}, 
 *				 	 "restriction": {"type": "string", "optional": true}, 
 *				 	 "virtualTuple":{"type": {"name": "VitualTuple", "properties": 
 *				 	 	{"pattern", {"type":"string"}, 
 *				 	 	 "virtualizationFunction":{"type":"string"}}, "optional" : true}
 *						}
 *					}
 *				}
 *			},
 *			{"name":"port", "type":"integer"}
 *		]
 *	}
 */
public class PutEventService extends AbstractEventService {

    @Override
    public String getName() {
        return "put";
    }
    
}
