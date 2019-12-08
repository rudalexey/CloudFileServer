import moment from "moment";
import {GLOBAL_FORMAT_DATE_TIME, GLOBAL_FORMAT_DATE_WITHOUT_TIME} from "../constants";

export function stringToDateAndTime(strDate) {
    return moment(strDate,GLOBAL_FORMAT_DATE_TIME).toDate();
}
export function stringToDateWithOutTime(strDate) {
    return moment(strDate,GLOBAL_FORMAT_DATE_WITHOUT_TIME).toDate();
}
