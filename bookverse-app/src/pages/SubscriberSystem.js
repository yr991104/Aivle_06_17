import React, { useState } from 'react';
import AppBar from '../components/AppBar';
import Footer from '../components/Footer';

const SubscriberSystem = () => {
  const [activeTab, setActiveTab] = useState('회원가입');

  const handleAction = (actionName) => {
    alert(`'${actionName}' 기능이 실행되었습니다.`);
  };

  const renderTabContent = () => {
    switch (activeTab) {
      case '회원가입':
        return (
          <div className="space-y-4 mt-6">
            <button onClick={() => handleAction('회원가입')} className="bg-blue-600 text-white px-4 py-2 rounded">회원가입</button>
          </div>
        );
      case '전자책 열람 요청':
        return (
          <div className="space-y-4 mt-6">
            <button onClick={() => handleAction('전자책 열람 요청')} className="bg-purple-600 text-white px-4 py-2 rounded">전자책 열람 요청</button>
          </div>
        );
      case '구독 관리':
        return (
          <div className="space-y-4 mt-6">
            <button onClick={() => handleAction('구독 신청')} className="bg-green-600 text-white px-4 py-2 rounded">구독 신청</button>
            <button onClick={() => handleAction('구독 취소')} className="bg-red-600 text-white px-4 py-2 rounded">구독 취소</button>
          </div>
        );
      case 'KT 멤버십 신청':
        return (
          <div className="space-y-4 mt-6">
            <button onClick={() => handleAction('KT 멤버십 신청')} className="bg-yellow-500 text-white px-4 py-2 rounded">KT 멤버십 신청</button>
          </div>
        );
      default:
        return <div className="text-gray-500 mt-6">기능을 선택해주세요.</div>;
    }
  };

  return (
    <div className="min-h-screen flex flex-col bg-white font-sans">
      <AppBar />
      <div className="flex flex-1">
        {/* Sidebar */}
        <aside className="w-64 bg-gray-100 p-4 min-h-full">
          <nav className="space-y-2">
            <div className="font-semibold text-gray-600">구독자 시스템</div>
            {['회원가입', '전자책 열람 요청', '구독 관리', 'KT 멤버십 신청'].map((tab) => (
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
          <h1 className="text-3xl font-bold mb-4">구독자 시스템</h1>
          <h2 className="text-xl font-semibold mb-2">{activeTab}</h2>
          {renderTabContent()}
        </main>
      </div>
      <Footer />
    </div>
  );
};

export default SubscriberSystem;
