import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import React from "react";
import {CsrfTokenProvider} from "@/shared/context";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Csrf Test",
  description: "Csrf 토큰을 테스트하기 위한 페이지입니다.",
};

type Props = {
    children: React.ReactNode
}

const RootLayout = ({children}: Props) => {
  return (
    <html lang="ko">
    <body className={inter.className}>
    <CsrfTokenProvider>
        {children}
    </CsrfTokenProvider>
    </body>
    </html>
  );
}

export default RootLayout;
