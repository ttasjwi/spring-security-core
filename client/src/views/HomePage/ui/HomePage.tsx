import {CsrfButton} from "@/widgets/CsrfButton";

export const HomePage = () => {
    return (
        <section className='flex flex-col items-center'>
            <h1 className='text-2xl font-bold mb-2'>Csrf 테스트</h1>
            <CsrfButton text={'요청시작'}/>
        </section>
    );
};
