'use client';

import React from 'react';
import {useCsrf} from "@/shared/context";

type Props = {
    text: string
}

const CsrfButton = ({text}: Props) => {
    const {csrfToken, isLoading, error} = useCsrf();
    const handleClick = (e: React.MouseEvent<HTMLButtonElement>) => {
        fetch("http://localhost:8080/requireCsrf", {
            method: 'POST',
            headers: {
                [csrfToken!!.headerName] : csrfToken!!.value
            },
            credentials: "include"
        }).then(res => {
            if (res.status >= 400) {
                throw Error("Csrf 토큰이 필요한 요청에 실패")
            }
            return res.text()
        }).then(data => {
            console.log(data)
        }).catch(err => {
            console.log(err)
        })
    }

    if (isLoading) {
        return <p>버튼 로딩 중...</p>
    }
    if (error) {
        return <p>버튼 로딩에 실패했습니다.</p>
    }

    return (
        <button className={'bg-red-400 py-2 px-4 text-white rounded-sm hover:brightness-110 disabled:bg-gray-500 disabled:brightness-100'} onClick={handleClick}>
            {text}
        </button>
    );
};

export {CsrfButton};
