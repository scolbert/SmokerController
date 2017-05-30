export function getPlans() {
    return ({
        type: "GET_PLANS",
        payload: [{id: '3', name: 'Plan Retrieved From Action'}, {id: '4', name:'Another Plan Returned From Action'}]
    });
}

export function addPlan(){
    return ({
        type: "PLAN_ADD",
        payload: {id:3, name:'Added Smoking Plan'}
    })
}