'use client';

import React, {createContext, useContext, useEffect, useState} from "react";

type Props = {
    children: React.ReactNode;
}

type CsrfToken = {
    headerName: string,
    parameterName: string,
    value: string
}

type CsrfTokenContextType = {
    csrfToken: CsrfToken | undefined,
    isLoading: boolean,
    error: Error | undefined
}

const CsrfTokenContext = createContext<CsrfTokenContextType>({
    csrfToken: undefined,
    isLoading: false,
    error: undefined
})

export const CsrfTokenProvider = ({children}: Props) => {
    const [csrfToken, setCsrfToken] = useState<CsrfToken | undefined>(undefined)
    const [isLoading, setIsLoading] = useState<boolean>(false)
    const [error, setError] = useState<Error | undefined>(undefined)

    useEffect(() => {
        setIsLoading(true)
        fetch("http://localhost:8080/csrf", {
            method: 'GET',
            credentials: 'include'
        }).then(res => {
            if (res.status >= 400) {
                throw Error("인증 csrf 토큰 로딩 실패")
            }
            return res.json()
        }).then(data => {
            setCsrfToken({
                headerName: data.headerName,
                parameterName: data.parameterName,
                value: data.token
            })
        }).catch(err => {
            // CsrfToken 로딩 실패 예외처리
            setError(err)
        }).finally(() => {
            setIsLoading(false)
        })
    }, []);

    return (
        <CsrfTokenContext.Provider value={{csrfToken, isLoading, error}}>
            {children}
        </CsrfTokenContext.Provider>
    );
}

export const useCsrf = () => useContext(CsrfTokenContext);
