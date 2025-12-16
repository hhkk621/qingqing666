import dayjs from 'dayjs'

export const formatTime = (time, format = 'YYYY-MM-DD HH:mm:ss') => {
    return dayjs(time).isValid()
        ? dayjs(time).format(format)
        : '无效时间'
}

export const formatLoginTime = (time, format = 'YYYY-MM-DD HH:mm:ss') => {
    return dayjs(time).isValid()
        ? dayjs(time).format(format)
        : '未登录'
}

export const formatDay = (time, format = 'YYYY-MM-DD') => {
    return dayjs(time).isValid()
        ? dayjs(time).format(format)
        : '无效时间'
}