import React, { useState, useEffect } from 'react';

const EbookWrite = () => {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');

  // 컴포넌트 로드 시 localStorage에서 기존 값 불러오기
  useEffect(() => {
    const savedTitle = localStorage.getItem('ebookTitle');
    const savedContent = localStorage.getItem('ebookContent');
    if (savedTitle) setTitle(savedTitle);
    if (savedContent) setContent(savedContent);
  }, []);

  const handleTempSave = () => {
    localStorage.setItem('ebookTitle', title);
    localStorage.setItem('ebookContent', content);
    alert('임시 저장 완료!');
  };

  const handlePublish = () => {
    if (!title || !content) {
      alert('제목과 콘텐츠를 모두 입력하세요.');
      return;
    }

    // TODO: 백엔드 출간 요청 로직
    alert('출간 요청 완료! (백엔드 연동 필요)');

    // 출간 완료 후 임시 저장 데이터 제거
    localStorage.removeItem('ebookTitle');
    localStorage.removeItem('ebookContent');

    // 작성 폼 초기화
    setTitle('');
    setContent('');
  };

  return (
    <div className="flex min-h-screen bg-white font-sans">
      {/* 사이드바 */}
      <aside className="w-64 bg-[#f0f2f5] p-6 border-r border-[#d0d7de]">
        <h2 className="text-xl font-bold mb-8">작가 시스템</h2>
        <nav className="flex flex-col gap-4">
          <a href="/author" className="text-[#111418] font-medium hover:underline">작가 등록 신청</a>
          <a href="/author/write" className="text-[#111418] font-medium hover:underline">전자책 작성</a>
          <a href="/author/list" className="text-[#111418] font-medium hover:underline">등록된 전자책 확인</a>
        </nav>
      </aside>

      {/* 본문 */}
      <div className="flex flex-col flex-1">
        <header className="flex items-center justify-between border-b border-[#f0f2f5] px-10 py-3">
          <div className="flex items-center gap-4 text-[#111418]">
            <div className="size-4">
              <svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path fillRule="evenodd" clipRule="evenodd" d="M24 4H6V17.3333V30.6667H24V44H42V30.6667V17.3333H24V4Z" fill="currentColor" />
              </svg>
            </div>
            <h2 className="text-lg font-bold tracking-[-0.015em]">StoryCraft</h2>
          </div>
        </header>

        <main className="px-20 py-8 flex justify-center">
          <div className="max-w-[960px] w-full">
            <h1 className="text-[32px] font-bold mb-6">전자책 작성</h1>

            <section className="mb-6">
              <div className="flex flex-col gap-4 max-w-[600px]">
                <label className="flex flex-col">
                  <p className="pb-2 text-base font-medium">제목</p>
                  <input
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    placeholder="제목을 입력하세요"
                    className="form-input bg-[#f0f2f5] h-14 rounded-lg p-4 placeholder-[#60758a]"
                  />
                </label>

                <label className="flex flex-col">
                  <p className="pb-2 text-base font-medium">콘텐츠</p>
                  <textarea
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                    placeholder="내용을 입력하세요"
                    className="form-input bg-[#f0f2f5] min-h-60 rounded-lg p-4 placeholder-[#60758a]"
                  />
                </label>
              </div>
            </section>

            <section>
              <div className="flex gap-3 pt-2">
                <button
                  onClick={handlePublish}
                  className="h-10 px-4 rounded-lg bg-[#0c7ff2] text-white font-bold"
                >
                  출간 요청하기
                </button>
                <button
                  onClick={handleTempSave}
                  className="h-10 px-4 rounded-lg bg-[#f0f2f5] text-[#111418] font-bold"
                >
                  임시 저장하기
                </button>
              </div>
            </section>
          </div>
        </main>
      </div>
    </div>
  );
};

export default EbookWrite;
