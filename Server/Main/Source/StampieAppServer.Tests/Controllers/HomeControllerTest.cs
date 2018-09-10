using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Text;
using System.Web.Mvc;

using Microsoft.VisualStudio.TestTools.UnitTesting;

using StampieAppServer;
using StampieAppServer.Controllers;
using StampieAppServer.Models;

namespace StampieAppServer.Tests.Controllers
{
    [TestClass]
    public class HomeControllerTest
    {
        [TestInitialize]
        public void TestInit()
        {
            Console.WriteLine("Test init");
        }

        [TestCleanup]
        public void TestDone()
        {
            Console.WriteLine("Test done");
        }

        [TestMethod]
        public void About()
        {
            Console.WriteLine("Test run started");

            // Arrange
            HomeController controller = new HomeController();

            // Act
            ViewResult result = controller.About() as ViewResult;

            // Assert
            Assert.AreEqual("Your application description page.", result.ViewBag.Message);

            Console.WriteLine("Test run finished");
        }
    }
}
