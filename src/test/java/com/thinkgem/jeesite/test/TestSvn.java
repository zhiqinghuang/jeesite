package com.thinkgem.jeesite.test;

import java.util.Collection;
import java.util.Iterator;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class TestSvn {

	public static void main(String[] args) {
		String url = "http://svn.svnkit.com/repos/svnkit/trunk/svnkit/";
		String name = "anonymous";
		String password = "anonymous";
		setupLibrary();
		SVNRepository repository = null;
		try {
			repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
		} catch (SVNException svne) {
			System.err.println("error while creating an SVNRepository for location '" + url + "': " + svne.getMessage());
			System.exit(1);
		}
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
		repository.setAuthenticationManager(authManager);
		try {
			SVNNodeKind nodeKind = repository.checkPath("", -1);
			if (nodeKind == SVNNodeKind.NONE) {
				System.err.println("There is no entry at '" + url + "'.");
				System.exit(1);
			} else if (nodeKind == SVNNodeKind.FILE) {
				System.err.println("The entry at '" + url + "' is a file while a directory was expected.");
				System.exit(1);
			}
			System.out.println("Repository Root: " + repository.getRepositoryRoot(true));
			System.out.println("Repository UUID: " + repository.getRepositoryUUID(true));
			System.out.println("");

			listEntries(repository, "");
		} catch (SVNException svne) {
			System.err.println("error while listing entries: " + svne.getMessage());
			System.exit(1);
		}
		long latestRevision = -1;
		try {
			latestRevision = repository.getLatestRevision();
		} catch (SVNException svne) {
			System.err.println("error while fetching the latest repository revision: " + svne.getMessage());
			System.exit(1);
		}
		System.out.println("");
		System.out.println("---------------------------------------------");
		System.out.println("Repository latest revision: " + latestRevision);
		System.exit(0);

	}

	private static void setupLibrary() {
		DAVRepositoryFactory.setup();
		SVNRepositoryFactoryImpl.setup();
		FSRepositoryFactory.setup();
	}

	public static void listEntries(SVNRepository repository, String path) throws SVNException {
		Collection entries = repository.getDir(path, -1, null, (Collection) null);
		Iterator iterator = entries.iterator();
		while (iterator.hasNext()) {
			SVNDirEntry entry = (SVNDirEntry) iterator.next();
			System.out.println("/" + (path.equals("") ? "" : path + "/") + entry.getName() + " (author: '" + entry.getAuthor() + "'; revision: " + entry.getRevision() + "; date: " + entry.getDate() + ")");
		}
	}

}
