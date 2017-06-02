// returns a string that describes the Turn Off Criteria
// if more or less than 3 probes are identified it returns a string that says Turn Off Criteria must specify exactly 3 probes

module.exports.createName = (temp, probeArray) => {
    console.log(probeArray.length);
    if(!(probeArray.length === 3)) {
        throw new Error('Turn Off Criteria must specify exactly 3 probes');
    }
    return ('Probes ' + probeArray[0] + ', ' + probeArray[1] + ' and ' + probeArray[2] + ' at ' + temp + ' degrees Farenheit' );
    // return ('Probes 2, 3 and 4 at 273 degrees Farenheit');
}