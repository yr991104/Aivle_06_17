import React, { useState, useEffect } from 'react';
import AppBar from '../components/AppBar';
import Footer from '../components/Footer';
import axios from 'axios';

const API_BASE = 'https://8084-bbobburi-aivle0617-hdvv9yt2jpn.ws-us120.gitpod.io';

const LibraryPlatform = () => {
  const [ebooks, setEbooks] = useState([]);
  const [form, setForm] = useState({ ebookId: '', ebooks: [], coverImage: '' });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  // 전자책 등록 (ebookId만 백엔드로 전달)
  const registerEbook = async () => {
    try {
      const payload = {
        ebookId: Number(form.ebookId), // 숫자형으로 변환
        ebooks: form.ebooks ? [form.ebooks] : [],  // 문자열을 배열로 변환
        coverImage: form.coverImage,
      };
      const response = await axios.post(`${API_BASE}/ebooks`, payload);
      alert(response.data.message || "등록 완료");
      fetchEbooks();
      setForm({ ebookId: '', ebooks: '', coverImage: '' });
    } catch (err) {
      console.error("❌ 등록 요청 실패", err);
      alert('등록 실패: ' + err.message);
    }
  };

  // 전자책 열람
  const [openMessage, setOpenMessage] = useState('');

  const openEbook = async (ebookId) => {
    try {
      const res = await axios.get(`${API_BASE}/ebooks/open/${ebookId}`);
      setOpenMessage(res.data);
    } catch (err) {
      setOpenMessage('열람 실패: ' + err.message);
    }
  };

  // 전자책 삭제 (비공개 처리)
  const removeEbook = async (ebookId) => {
    try {
      const res = await axios.delete(`${API_BASE}/ebooks/remove/${ebookId}`);
      alert(res.data);
      fetchEbooks();
    } catch (err) {
      alert('비공개 실패: ' + err.message);
    }
  };

  // 전자책 목록 조회
  const fetchEbooks = async () => {
    try {
      const res = await axios.get(`${API_BASE}/ebooks/all`);
      setEbooks(res.data);
    } catch (err) {
      console.error('목록 불러오기 실패:', err);
    }
  };

  // 최초 렌더링 시 데이터 불러오기
  useEffect(() => {
    fetchEbooks();
  }, []);

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
              <input name="ebookId" value={form.ebookId} onChange={handleChange} placeholder="전자책 ID" className="border p-2 rounded" />
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
                    <td className="p-2">{book.ebookId}</td>
                    <td className="p-2">{book.ebooks}</td>
                    <td className="p-2">{book.aiGeneratedCover}</td>
                    <td className="p-2">{book.registeredAt}</td>
                    <td className="p-2 space-x-2">
                      <button onClick={() => openEbook(book.ebookId)} className="text-blue-600">열람</button>
                      <button onClick={() => removeEbook(book.ebookId)} className="text-red-500">비공개 요청</button>
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
