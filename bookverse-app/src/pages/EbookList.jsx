import React, { useEffect, useState } from 'react';
import axios from 'axios';

const EbookList = () => {
  const [ebooks, setEbooks] = useState([]);

  useEffect(() => {
    // TODO: 내 전자책 목록 불러오기
    axios.get('/api/ebooks/mine') // 필요하면 본인 API 경로로 교체
      .then(res => setEbooks(res.data))
      .catch(err => console.error(err));
  }, []);

  const handleCancelRequest = (ebookId) => {
    if (window.confirm('출간 요청을 취소하시겠습니까?')) {
      axios.post(`/api/ebooks/${ebookId}/cancel`)
        .then(() => {
          alert('출간 요청이 취소되었습니다.');
          setEbooks(ebooks.filter(e => e.id !== ebookId));
        })
        .catch(err => {
          console.error(err);
          alert('요청 취소에 실패했습니다.');
        });
    }
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
            <h1 className="text-[32px] font-bold mb-6">등록된 전자책 확인</h1>

            <table className="w-full text-left border border-[#d0d7de]">
              <thead className="bg-[#f0f2f5]">
                <tr>
                  <th className="p-3 border-b">ID</th>
                  <th className="p-3 border-b">제목</th>
                  <th className="p-3 border-b">AuthorId</th>
                  <th className="p-3 border-b">AuthorName</th>
                  <th className="p-3 border-b">출간 상태</th>
                  <th className="p-3 border-b"></th>
                </tr>
              </thead>
              <tbody>
                {ebooks.map((ebook) => (
                  <tr key={ebook.id} className="border-t">
                    <td className="p-3">{ebook.id}</td>
                    <td className="p-3">{ebook.title}</td>
                    <td className="p-3">{ebook.authorId}</td>
                    <td className="p-3">{ebook.authorName}</td>
                    <td className="p-3">{ebook.publicationStatus}</td>
                    <td className="p-3">
                      <button
                        onClick={() => handleCancelRequest(ebook.id)}
                        className="px-3 py-1 rounded bg-[#f87171] text-white text-sm"
                      >
                        출간 요청 취소
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>

          </div>
        </main>
      </div>
    </div>
  );
};

export default EbookList;
