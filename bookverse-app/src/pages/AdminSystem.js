import React, { useState } from 'react';
import AppBar from '../components/AppBar';
import Footer from '../components/Footer';

const AdminSystem = () => {
  const [activeTab, setActiveTab] = useState('승인 요청');

  const handleAction = (action) => {
    alert(`'${action}' 기능이 실행되었습니다.`);
  };

  const renderTabContent = () => {
    if (activeTab === '승인 요청') {
      return (
        <div className="mt-6 space-y-4">
          <button onClick={() => handleAction('작가 등록 승인')} className="bg-blue-500 text-white px-4 py-2 rounded">작가 등록 승인</button>
        </div>
      );
    } else if (activeTab === '출간 요청') {
      return (
        <div className="mt-6 space-y-4">
          <button onClick={() => handleAction('전자책 출간 승인')} className="bg-green-500 text-white px-4 py-2 rounded">전자책 출간 승인</button>
        </div>
      );
    } else if (activeTab === '비공개 처리') {
      return (
        <div className="mt-6 space-y-4">
          <button onClick={() => handleAction('전자책 비공개 처리')} className="bg-red-500 text-white px-4 py-2 rounded">전자책 비공개 처리</button>
        </div>
      );
    }
    return <div className="text-gray-400 mt-6">기능을 선택하세요.</div>;
  };

  return (
    <div className="min-h-screen flex flex-col bg-white font-sans">
      <AppBar />
      <div className="flex flex-1">
        {/* Sidebar */}
        <aside className="w-64 bg-gray-100 min-h-full p-4">
          <nav className="space-y-2">
            <div className="font-semibold text-gray-600">관리자 시스템</div>
            {['승인 요청', '출간 요청', '비공개 처리'].map((tab) => (
              <div
                key={tab}
                onClick={() => setActiveTab(tab)}
                className={`p-2 rounded cursor-pointer ${activeTab === tab ? 'bg-white shadow font-bold' : 'text-gray-500'}`}
              >
                {tab}
              </div>
            ))}
          </nav>
        </aside>

        {/* Main Content */}
        <main className="flex-1 p-10">
          <h1 className="text-3xl font-bold mb-4">관리자 시스템</h1>
          <h2 className="text-xl font-semibold mb-2">{activeTab}</h2>
          {renderTabContent()}
        </main>
      </div>
      <Footer />
    </div>
  );
};

export default AdminSystem;
