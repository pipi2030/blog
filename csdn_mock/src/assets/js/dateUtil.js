import moment from 'moment'

const DEFAULT_FORMAT = {
    date: 'YYYY-MM-DD HH:mm:ss'
}

let dateUtil = {}

dateUtil = {
    format (time, format) {
        if (time !== null && time !== undefined) {
            if (format === null || format === undefined) {
                format = DEFAULT_FORMAT.date
            }
            return moment(time).format(format)
        }
    },
    timestamp (obj, format) {
        if (obj !== undefined && obj !== null) {
            return moment(obj, format).valueOf()
        }
    },
    date (obj, format) {
        if (obj !== undefined && obj !== null) {
            return moment(obj, format).toDate()
        }
    },
    addMinutes (time, minutes, format) {
        if (time !== undefined && time !== null) {
            return moment(time, format).add(minutes, 'm').valueOf()
        }
    },
    addHours (time, hours, format) {
        if (time !== undefined && time !== null) {
            return moment(time, format).add(hours, 'h').valueOf()
        }
    },
    addDays (time, days, format) {
        if (time !== undefined && time !== null) {
            return moment(time, format).add(days, 'd').valueOf()
        }
    },
    addWeeks (time, weeks, format) {
        if (time !== undefined && time !== null) {
            return moment(time, format).add(weeks, 'w').valueOf()
        }
    },
    addMonths (time, months, format) {
        if (time !== undefined && time !== null) {
            return moment(time, format).add(months, 'M').valueOf()
        }
    },
    addYears (time, years, format) {
        if (time !== undefined && time !== null) {
            return moment(time, format).add(years, 'y').valueOf()
        }
    },
    today () {
        return moment().startOf('day').valueOf()
    },
    now () {
        return new Date().getTime()
    },
    /**
     * 获取开始日期时间戳
     * 如：获取昨天的开始时间戳
     * this.$DateUtil.startOf('day', -1)
     * 如：获取月的开始时间戳
     * this.$DateUtil.startOf('Month')
     * @param {*} type day;week;Month;Year
     * @param {*} num 前后的差数
     * @returns
     */
    startOf (type, num) {
        if (num === undefined) {
            num = 0
        }
        return moment().add(num, type.substr(0, 1)).startOf(type).valueOf()
    },
    /**
     * 获取结束日期时间戳
     * 如：获取昨天的开始时间戳
     * this.$DateUtil.endOf('day', -1)
     * 如：获取月的结束时间戳
     * this.$DateUtil.endOf('Month')
     * @param {*} type day;week;Month;Year
     * @param {*} num 前后的差数
     * @returns
     */
    endOf (type, num) {
        if (num === undefined) {
            num = 0
        }
        return moment().add(num, type.substr(0, 1)).endOf(type).valueOf()
    }
}
export default dateUtil
