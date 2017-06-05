const namer =  require('../../helpers/TurnOffCriteriaNamer.js');
const assert = require('assert');

describe('createName', function(){
    it('returns Probes 2, 3 and 4 at 273 degrees Farenheit when provided 273 and [2, 3, 4] as arguments', function(){
        assert(namer.createName(273, [2, 3, 4]) === 'Probes 2, 3 and 4 at 273 degrees Farenheit');
    });

    it('returns Probes 1, 2 and 3 at 300 degrees Farenheit when provided 300 and [1, 2, 3] as arguments', function(){
        assert(namer.createName(300, [1, 2, 3]) === 'Probes 1, 2 and 3 at 300 degrees Farenheit');
    });

    // this one doesn't work because assert.throws is a node module and I am not running node.
    it('returns Turn Off Criteria must specify exactly 3 probes when provided with less than 3 probes', function(){
        assert.throws(
            namer.createName(300, [1, 2]),
            /Turn Off Criteria must specify exactly 3 probes/
        );
    })
})