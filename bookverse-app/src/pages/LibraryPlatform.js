import React, { useState } from 'react';
import AppBar from '../components/AppBar';
import Footer from '../components/Footer';

const LibraryPlatform = () => {
  const [ebooks, setEbooks] = useState([]);
  const [form, setForm] = useState({ pid: '', ebooks: '', aiGeneratedCover: '' });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const registerEbook = () => {
    const newBook = {
      pid: form.pid,
      ebooks: form.ebooks,
      aiGeneratedCover: form.aiGeneratedCover,
      registeredAt: new Date().toISOString(),
    };
    setEbooks([...ebooks, newBook]);
    setForm({ pid: '', ebooks: '', aiGeneratedCover: '' });
  };

  const removeEbook = (pid) => {
    setEbooks(ebooks.filter((book) => book.pid !== pid));
  };

  return (
    <div className="min-h-screen flex flex-col bg-white font-sans">
      <AppBar />
      <div className="flex flex-1">
        <aside className="w-64 bg-gray-100 min-h-full p-4">
          <nav className="space-y-2">
            <div className="font-semibold text-gray-600">Dashboard</div>
            <div className="bg-white p-2 rounded shadow font-bold">E-books</div>
            <div className="text-gray-500">Users</div>
            <div className="text-gray-500">Analytics</div>
            <div className="text-gray-500">Settings</div>
          </nav>
        </aside>

        <main className="flex-1 p-10">
          <h1 className="text-3xl font-bold">서재 플랫폼</h1>
          <p className="text-gray-600 mb-6">전자책 상태 체크, 등록, 열람, 비공개 요청 기반 삭제 기능 제공</p>

          {/* 전자책 등록 폼 */}
          <div className="mb-6 border p-4 rounded">
            <h2 className="text-xl font-semibold mb-2">전자책 등록</h2>
            <div className="flex flex-col gap-2">
              <input name="pid" value={form.pid} onChange={handleChange} placeholder="전자책 ID" className="border p-2 rounded" />
              <input name="ebooks" value={form.ebooks} onChange={handleChange} placeholder="전자책 제목" className="border p-2 rounded" />
              <input name="aiGeneratedCover" value={form.aiGeneratedCover} onChange={handleChange} placeholder="AI 생성 커버 URL" className="border p-2 rounded" />
              <button onClick={registerEbook} className="bg-blue-600 text-white px-4 py-2 rounded">등록</button>
            </div>
          </div>

          {/* 등록된 전자책 목록 */}
          <div>
            <h2 className="text-xl font-semibold mb-2">등록된 전자책 목록</h2>
            <table className="w-full table-auto border-collapse">
              <thead>
                <tr className="bg-gray-100 text-left">
                  <th className="p-2">ID</th>
                  <th className="p-2">Title</th>
                  <th className="p-2">AI Cover</th>
                  <th className="p-2">Registered At</th>
                  <th className="p-2">Actions</th>
                </tr>
              </thead>
              <tbody>
                {ebooks.map((book, index) => (
                  <tr key={index} className="border-b">
                    <td className="p-2">{book.pid}</td>
                    <td className="p-2">{book.ebooks}</td>
                    <td className="p-2">{book.aiGeneratedCover}</td>
                    <td className="p-2">{book.registeredAt}</td>
                    <td className="p-2 space-x-2">
                      <button className="text-blue-600">열람</button>
                      <button onClick={() => removeEbook(book.pid)} className="text-red-500">비공개 요청</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </main>
      </div>
      <Footer />
    </div>
  );
};

export default LibraryPlatform;
